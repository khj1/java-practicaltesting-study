package sample.cafekiosk.spring.domain.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

/**
 * 시나리오를 정해서 환경을 조성하고 시나리오 대로 순차적으로 테스트할 수 있다.
 */
class StockTest {

	@DisplayName("재고 차감 시나리오")
	@TestFactory
	Collection<DynamicTest> stockDeductionDynamicTest() {
		// given
		Stock stock = Stock.create("001", 1);

		return List.of(
				DynamicTest.dynamicTest("재고를 주어진 개수만큼 차감할 수 있다.", () -> {
					// given
					int quantity = 1;

					// when
					stock.deductQuantity(quantity);

					// then
					assertThat(stock.getQuantity()).isZero();
				}),
				
				DynamicTest.dynamicTest("재고보다 많은 수량을 차감할 수 없다.", () -> {
					// given
					int quantity = 1;

					// when / then
					assertThatThrownBy(() -> stock.deductQuantity(quantity))
							.isInstanceOf(IllegalArgumentException.class)
							.hasMessage("차감할 재고 수량이 없습니다.");
				})
		);
	}

}
