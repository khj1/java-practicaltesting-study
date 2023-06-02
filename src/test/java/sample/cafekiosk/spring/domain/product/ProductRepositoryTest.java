package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.HOLD;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.STOP_SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Repository Test는 사실상 단위 테스트에 가깝다.
 * - SpringBootTest vs DataJpaTest
 * - DataJpaTest가 Jpa 관련 빈들만 로딩하기 때문에 더 가볍지만 강사님은 SpringBootTest를 더 선호한다.
 */
@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@DisplayName("판매 중이거나 판매 보류 중인 상품들을 조회한다.")
	@Test
	void findAllBySellingStatusIn() {
		// given
		Product productA = Product.builder()
				.productNumber("001")
				.type(HANDMADE)
				.sellingStatus(SELLING)
				.name("아메리카노")
				.price(4_000)
				.build();

		Product productB = Product.builder()
				.productNumber("002")
				.type(HANDMADE)
				.sellingStatus(HOLD)
				.name("카페라떼")
				.price(4_000)
				.build();

		Product productC = Product.builder()
				.productNumber("003")
				.type(HANDMADE)
				.sellingStatus(STOP_SELLING)
				.name("팥빙수")
				.price(4_000)
				.build();

		productRepository.saveAll(List.of(productA, productB, productC));

		// when
		List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

		//then
		assertThat(products)
				.hasSize(2)
				.extracting("productNumber", "name", "sellingStatus")
				.containsExactlyInAnyOrder(
						tuple("001", "아메리카노", SELLING),
						tuple("002", "카페라떼", HOLD)
				);

	}

	@DisplayName("상품 고유번호로 상품을 조회할 수 있다.")
	@Test
	void findAllByProductNumberIn() {
		// given
		Product productA = Product.builder()
				.productNumber("001")
				.type(HANDMADE)
				.sellingStatus(SELLING)
				.name("아메리카노")
				.price(4_000)
				.build();

		Product productB = Product.builder()
				.productNumber("002")
				.type(HANDMADE)
				.sellingStatus(HOLD)
				.name("카페라떼")
				.price(4_000)
				.build();

		Product productC = Product.builder()
				.productNumber("003")
				.type(HANDMADE)
				.sellingStatus(STOP_SELLING)
				.name("팥빙수")
				.price(4_000)
				.build();

		productRepository.saveAll(List.of(productA, productB, productC));

		// when
		List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

		//then
		assertThat(products).hasSize(2)
				.extracting("productNumber", "name")
				.containsExactlyInAnyOrder(
						tuple("001", "아메리카노"),
						tuple("002", "카페라떼")
				);

	}

}
