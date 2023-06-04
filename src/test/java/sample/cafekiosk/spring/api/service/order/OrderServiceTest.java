package sample.cafekiosk.spring.api.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.BAKERY;
import static sample.cafekiosk.spring.domain.product.ProductType.BOTTLE;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderProductRepository orderProductRepository;
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductRepository productRepository;

	/**
	 * @Transactional 을 사용해서 자동으로 해당 작업을 실행할 수 있지만
	 * @Transactional 을 사용해서 발생하는 문제점도 있다.
	 * 	- 테스트에 붙여놓은 @Transactional 어노테이션 때문에 실제 서비스 로직에도 @Transactional
	 * 		이 적용된 것처럼 착각할 수 있다.
	 */
	@BeforeEach
	void setUp() {
		orderProductRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		stockRepository.deleteAllInBatch();
	}

	@DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
	@Test
	void createOrder() {
		// given
		Product productA = createProduct(HANDMADE, "001", 1_000);
		Product productB = createProduct(BAKERY, "002", 2_000);
		Product productC = createProduct(HANDMADE, "003", 3_000);
		productRepository.saveAll(List.of(productA, productB, productC));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
				.productNumbers(List.of("001", "002"))
				.build();

		Stock stockA = Stock.create("001", 2);
		Stock stockB = Stock.create("002", 1);
		stockRepository.saveAll(List.of(stockA, stockB));

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

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
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

	@DisplayName("재고와 관련된 상품이 포함되어 있는 경우 주문 생성 시 재고가 차감된다.")
	@Test
	void createOrderWithStock() {
		// given
		Product productA = createProduct(BOTTLE, "001", 1_000);
		Product productB = createProduct(BAKERY, "002", 2_000);
		Product productC = createProduct(HANDMADE, "003", 3_000);
		productRepository.saveAll(List.of(productA, productB, productC));

		Stock stockA = Stock.create("001", 2);
		Stock stockB = Stock.create("002", 1);

		stockRepository.saveAll(List.of(stockA, stockB));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
				.productNumbers(List.of("001", "001", "002", "003"))
				.build();

		// when
		LocalDateTime registerDateTime = LocalDateTime.now();
		OrderResponse orderResponse = orderService.createOrder(request, registerDateTime);

		//then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
				.extracting("registerDateTime", "totalPrice")
				.contains(registerDateTime, 7_000);

		assertThat(orderResponse.getProducts()).hasSize(4)
				.extracting("productNumber", "price")
				.containsExactlyInAnyOrder(
						tuple("001", 1_000),
						tuple("001", 1_000),
						tuple("002", 2_000),
						tuple("003", 3_000)
				);

		List<Stock> stocks = stockRepository.findAll();
		assertThat(stocks).hasSize(2)
				.extracting("productNumber", "quantity")
				.containsExactlyInAnyOrder(
						tuple("001", 0),
						tuple("002", 0)
				);
	}

	/**
	 * LocalDateTime.now()는 완전히 통제된 값이 아니다.
	 * 외부 환경에 따라 변하는 값이고 이로 인해 테스트에 영향을 끼칠 수도 있다.
	 *
	 * 테스트 환경에서는 팩토리 메서드를 지양하는 것이 좋다.
	 * - 팩토리 메서드는 프로덕션 코드에서 분명한 의도를 가지고 만들어지는 경우가 대부분이다.
	 * - 팩토리 메서드 내부에 의도하지 않은 검증 로직이 섞여있을 수도 있다.
	 * - 따라서 테스트 환경에선 순수한 생성자를 사용하는 것이 더 좋다.
	 */
	@DisplayName("재고가 없는 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
	@Test
	void createOrderWithNoStock() {
		// given
		Product productA = createProduct(BOTTLE, "001", 1_000);
		Product productB = createProduct(BAKERY, "002", 2_000);
		Product productC = createProduct(HANDMADE, "003", 3_000);
		productRepository.saveAll(List.of(productA, productB, productC));

		Stock stockA = Stock.create("001", 1);
		Stock stockB = Stock.create("002", 1);
		stockRepository.saveAll(List.of(stockA, stockB));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
				.productNumbers(List.of("001", "001", "002", "003"))
				.build();

		LocalDateTime registerDateTime = LocalDateTime.now();

		// when / then
		assertThatThrownBy(() -> orderService.createOrder(request, registerDateTime))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("재고가 부족합니다.");
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
