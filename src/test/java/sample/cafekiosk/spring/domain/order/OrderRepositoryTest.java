package sample.cafekiosk.spring.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafekiosk.spring.domain.order.OrderStatus.COMPLETED;
import static sample.cafekiosk.spring.domain.order.OrderStatus.INIT;
import static sample.cafekiosk.spring.domain.order.OrderStatus.PAYMENT_COMPLETED;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@SpringBootTest
class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@DisplayName("주문 상태와 주문 일자를 기준으로 주문을 조회할 수 있다.")
	@Test
	void test() {
		// given
		Product productA = createProduct("001", HANDMADE, "아메리카노", 1_000, SELLING);
		Product productB = createProduct("002", HANDMADE, "카푸치노", 2_000, SELLING);
		Product productC = createProduct("003", HANDMADE, "카페라떼", 3_000, SELLING);
		productRepository.saveAll(List.of(productA, productB, productC));

		Order orderA = Order.create(List.of(productA, productB), PAYMENT_COMPLETED,
				LocalDateTime.of(2023, 6, 4, 12, 30));
		Order orderB = Order.create(List.of(productA, productC), COMPLETED, LocalDateTime.of(2023, 6, 4, 13, 0));
		Order orderC = Order.create(List.of(productB, productC), INIT, LocalDateTime.of(2023, 6, 4, 12, 0));
		orderRepository.saveAll(List.of(orderA, orderB, orderC));

		LocalDateTime startDateTime = LocalDateTime.of(2023, 6, 4, 12, 0);
		LocalDateTime endDateTime = LocalDateTime.of(2023, 6, 4, 13, 0);

		// when
		List<Order> orders = orderRepository.findOrdersBy(startDateTime, endDateTime, PAYMENT_COMPLETED);

		//then
		assertThat(orders).hasSize(1)
				.extracting("orderStatus", "totalPrice")
				.containsExactly(
						tuple(PAYMENT_COMPLETED, 3_000)
				);

	}

	private Product createProduct(final String productNumber, final ProductType type, final String name,
								  final int price, final ProductSellingStatus sellingStatus) {
		return Product.builder()
				.productNumber(productNumber)
				.type(type)
				.sellingStatus(sellingStatus)
				.name(name)
				.price(price)
				.build();
	}
}
