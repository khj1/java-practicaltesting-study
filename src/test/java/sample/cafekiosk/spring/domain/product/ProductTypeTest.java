package sample.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * 테스트는 한 문단에 하나의 검증만 이루어지는 것이 좋다.
 *
 * @ParameterizedTest
 * - 하나의 기능에 여러 값을 반복적으로 테스트해보고 싶을 때 사용한다.
 */
class ProductTypeTest {

	@DisplayName("상품 타입이 재고에 관한 타입인지 체크한다.")
	@CsvSource({"HANDMADE,false", "BOTTLE,true", "BAKERY,true"})
	@ParameterizedTest
	void containsStockTypeWithBottle(ProductType productType, boolean expected) {
		// given / when
		boolean actual = ProductType.containsStockType(productType);

		//then
		Assertions.assertThat(actual).isEqualTo(expected);
	}
}
