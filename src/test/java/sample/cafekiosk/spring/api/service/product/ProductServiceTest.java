package sample.cafekiosk.spring.api.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@DisplayName("신규 상품을 등록한다. 상품 번호는 가장 최근 상품번호에서 1 증가한 값이다.")
	@Test
	void createProduct() {
		// given
		Product product = createProduct("001", HANDMADE, "아메리카노", 4_000, SELLING);
		productRepository.save(product);

		ProductCreateRequest request = ProductCreateRequest.builder()
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
		ProductCreateRequest request = ProductCreateRequest.builder()
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
