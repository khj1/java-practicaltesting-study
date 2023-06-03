package sample.cafekiosk.spring.api.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.BAKERY;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import sample.cafekiosk.spring.api.service.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderServiceTest {

	@Autowired
	private OrderProductRepository orderProductRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductRepository productRepository;

	/**
	 * @Transactional 을 사용해서 자동으로 해당 작업을 실행할 수 있지만
	 * @Transactional 을 사용해서 발생하는 문제점도 있다.
	 */
	// @AfterEach
	// void tearDown() {
	// 	orderProductRepository.deleteAllInBatch();
	// 	productRepository.deleteAllInBatch();
	// 	orderRepository.deleteAllInBatch();
	// }
	@DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
	@Test
	void createOrder() {
		// given
		Product productA = createProduct(HANDMADE, "001", 1_000);
		Product productB = createProduct(BAKERY, "002", 2_000);
		Product productC = createProduct(HANDMADE, "003", 3_000);
		productRepository.saveAll(List.of(productA, productB, productC));

		OrderCreateRequest request = OrderCreateRequest.builder()
				.productNumbers(List.of("001", "002"))
				.build();

		// when
		LocalDateTime registerDateTime = LocalDateTime.now();
		OrderResponse orderResponse = orderService.createOrder(request, registerDateTime);

		//then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
				.extracting("registerDateTime", "totalPrice")
				.contains(registerDateTime, 3_000);

		assertThat(orderResponse.getProducts()).hasSize(2)
				.extracting("productNumber", "price")
				.containsExactlyInAnyOrder(
						tuple("001", 1_000),
						tuple("002", 2_000)
				);
	}

	@DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
	@Test
	void createOrderWithDuplicatedProductNumbers() {
		// given
		Product productA = createProduct(HANDMADE, "001", 1_000);
		Product productB = createProduct(BAKERY, "002", 2_000);
		Product productC = createProduct(HANDMADE, "003", 3_000);
		productRepository.saveAll(List.of(productA, productB, productC));

		OrderCreateRequest request = OrderCreateRequest.builder()
				.productNumbers(List.of("001", "001"))
				.build();

		// when
		LocalDateTime registerDateTime = LocalDateTime.now();
		OrderResponse orderResponse = orderService.createOrder(request, registerDateTime);

		//then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
				.extracting("registerDateTime", "totalPrice")
				.contains(registerDateTime, 2_000);

		assertThat(orderResponse.getProducts()).hasSize(2)
				.extracting("productNumber", "price")
				.containsExactlyInAnyOrder(
						tuple("001", 1_000),
						tuple("001", 1_000)
				);

	}

	private Product createProduct(ProductType type, String productNumber, int price) {
		return Product.builder()
				.productNumber(productNumber)
				.type(type)
				.sellingStatus(SELLING)
				.name("메뉴 이름")
				.price(price)
				.build();
	}
}
