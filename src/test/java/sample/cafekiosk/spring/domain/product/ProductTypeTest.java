package sample.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTypeTest {

	@DisplayName("타입이 병 음료 또는 베이커리라면 재고 타입에 해당한다.")
	@Test
	void containsStockType() {
		// given
		ProductType bottle = ProductType.BOTTLE;
		ProductType bakery = ProductType.BAKERY;
		ProductType handmade = ProductType.HANDMADE;

		// when
		boolean bottleResult = ProductType.containsStockType(bottle);
		boolean bakeryResult = ProductType.containsStockType(bakery);
		boolean handMadeResult = ProductType.containsStockType(handmade);

		//then
		Assertions.assertThat(bottleResult).isTrue();
		Assertions.assertThat(bakeryResult).isTrue();
		Assertions.assertThat(handMadeResult).isFalse();
	}
}
