package sample.cafekiosk.spring.api.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	void setUp() {
		/**
		 * @BeforeEach 에서 given 절에서 공통적으로 사용되는 변수를 한 데 모아 선언해줄 수도 있다.
		 * - 하지만 이렇게 공유 변수를 사용하게되면 테스트 간 독립성이 보장될 수 있기 때문에 지양하는 것이 좋다.
		 * - 또한 문서로써의 테스트의 역할을 고려했을 때도 given 절에 fixture 들이 나열되어 있는 것이 가독성에 더 좋다.
		 * - 테스트를 읽어 내려갈 때 내가 지금 무엇을 테스트하고 있는지 명확하게 인지할 수 있게 작성하는 것이 중요하다.
		 *
		 * BeforeClass는 다음과 같은 경우에는 사용해도 좋다.
		 * - 각 테스트 입장에서 봤을 때 아예 몰라도 테스트 내용을 이해할 때 문제가 없는 경우
		 * - 수정해도 모든 테스트에 영향을 주지 않는 경우
		 */
	}

	@AfterEach
	void tearDown() {
		productRepository.deleteAllInBatch();
	}

	@DisplayName("신규 상품을 등록한다. 상품 번호는 가장 최근 상품번호에서 1 증가한 값이다.")
	@Test
	void createProduct() {
		// given
		Product product = createSimpleProduct();
		productRepository.save(product);

		ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
				.type(HANDMADE)
				.sellingStatus(SELLING)
				.name("카푸치노")
				.price(5_000)
				.build();

		// when
		ProductResponse response = productService.createProduct(request);

		//then
		assertThat(response)
				.extracting("productNumber", "type", "sellingStatus", "price", "name")
				.contains("002", HANDMADE, SELLING, 5_000, "카푸치노");

		List<Product> products = productRepository.findAll();
		assertThat(products).hasSize(2)
				.extracting("productNumber", "type", "sellingStatus", "price", "name")
				.containsExactlyInAnyOrder(
						tuple("001", HANDMADE, SELLING, 4_000, "아메리카노"),
						tuple("002", HANDMADE, SELLING, 5_000, "카푸치노")
				);
	}

	@DisplayName("신규 상품을 등록한다. 첫 상품인 경우 상품번호는 001번이다.")
	@Test
	void createFirstProduct() {
		// given
		ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
				.type(HANDMADE)
				.sellingStatus(SELLING)
				.name("카푸치노")
				.price(5_000)
				.build();

		// when
		ProductResponse response = productService.createProduct(request);

		//then
		assertThat(response)
				.extracting("productNumber", "type", "sellingStatus", "price", "name")
				.contains("001", HANDMADE, SELLING, 5_000, "카푸치노");

		List<Product> products = productRepository.findAll();
		assertThat(products).hasSize(1)
				.extracting("productNumber", "type", "sellingStatus", "price", "name")
				.containsExactlyInAnyOrder(
						tuple("001", HANDMADE, SELLING, 5_000, "카푸치노")
				);
	}

	/**
	 * 테스트에 의미있는 파라미터만 남겨두고 불필요한 필드는 직접 값을 넣어주는 것이 좋다.
	 */
	private Product createSimpleProduct() {
		return Product.builder()
				.productNumber("001")
				.type(HANDMADE)
				.sellingStatus(SELLING)
				.name("아메리카노")
				.price(4_000)
				.build();
	}
}
