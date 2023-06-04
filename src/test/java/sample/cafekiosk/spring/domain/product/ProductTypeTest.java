package sample.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 테스트는 한 문단에 하나의 검증만 이루어지는 것이 좋다.
 */
class ProductTypeTest {

	@DisplayName("타입이 병 라면 재고 타입에 해당한다.")
	@Test
	void containsStockTypeWithBottle() {
		// given
		ProductType bottle = ProductType.BOTTLE;

		// when
		boolean bottleResult = ProductType.containsStockType(bottle);

		//then
		Assertions.assertThat(bottleResult).isTrue();
	}

	@DisplayName("타입이 병 음료 또는 베이커리라면 재고 타입에 해당한다.")
	@Test
	void containsStockTypeWithBakery() {
		// given
		ProductType bakery = ProductType.BAKERY;

		// when
		boolean bakeryResult = ProductType.containsStockType(bakery);

		//then
		Assertions.assertThat(bakeryResult).isTrue();
	}

	@DisplayName("타입이 수제라면 재고 타입에 해당하지 않는다.")
	@Test
	void containsStockType() {
		// given
		ProductType handmade = ProductType.HANDMADE;

		// when
		boolean handMadeResult = ProductType.containsStockType(handmade);

		//then
		Assertions.assertThat(handMadeResult).isFalse();
	}
}
