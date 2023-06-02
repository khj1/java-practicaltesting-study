package sample.cafekiosk.spring.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sample.cafekiosk.spring.domain.product.Product;

class OrderTest {

	@DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
	@Test
	void calculateTotalPrice() {
		// given
		List<Product> products = List.of(
				createProduct("001", 1_000),
				createProduct("002", 2_000)
		);

		// when
		Order order = Order.create(products, LocalDateTime.now());

		//then
		assertThat(order.getTotalPrice()).isEqualTo(3_000);
	}

	@DisplayName("주문 생성 시 주문 상태는 INIT 이다.")
	@Test
	void initStatus() {
		// given
		List<Product> products = List.of(
				createProduct("001", 1_000),
				createProduct("002", 2_000)
		);

		// when
		Order order = Order.create(products, LocalDateTime.now());

		//then
		assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
	}

	@DisplayName("주문 생성 시 등록 시간을 기록한다.")
	@Test
	void registeredDateTime() {
		// given
		List<Product> products = List.of(
				createProduct("001", 1_000),
				createProduct("002", 2_000)
		);

		// when
		LocalDateTime registerDateTime = LocalDateTime.now();
		Order order = Order.create(products, registerDateTime);

		//then
		assertThat(order.getRegisterDateTime()).isEqualTo(registerDateTime);
	}

	private Product createProduct(String productNumber, int price) {
		return Product.builder()
				.productNumber(productNumber)
				.type(HANDMADE)
				.sellingStatus(SELLING)
				.name("메뉴 이름")
				.price(price)
				.build();
	}

}
