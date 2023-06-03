package sample.cafekiosk.spring.domain.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class StockRepositoryTest {

	@Autowired
	private StockRepository stockRepository;

	@DisplayName("상품 번호로 재고를 조회할 수 있다.")
	@Test
	void test() {
		// given
		Stock stockA = Stock.create("001", 1);
		Stock stockB = Stock.create("002", 2);
		Stock stockC = Stock.create("003", 3);
		stockRepository.saveAll(List.of(stockA, stockB, stockC));

		// when
		List<Stock> stocks = stockRepository.findByProductNumberIn(List.of("001", "002"));

		//then
		assertThat(stocks).hasSize(2)
				.extracting("productNumber", "quantity")
				.containsExactlyInAnyOrder(
						tuple("001", 1),
						tuple("002", 2)
				);
	}

	@DisplayName("재고의 수량이 제공된 수량보다 적은지 확인한다.")
	@Test
	void isQuantityLessThan() {
		// given
		Stock stock = Stock.create("001", 1);

		// when
		boolean resultA = stock.isQuantityLessThan(2);
		boolean resultB = stock.isQuantityLessThan(1);

		//then
		assertThat(resultA).isTrue();
		assertThat(resultB).isFalse();
	}

	@DisplayName("재고의 수량을 제공된 수량만큼 차감한다.")
	@Test
	void deductQuantity() {
		// given
		Stock stock = Stock.create("001", 2);

		// when
		stock.deductQuantity(1);

		//then
		assertThat(stock.getQuantity()).isEqualTo(1);
	}

	@DisplayName("재고보다 많은 수의 수량으로 차감 시도하는 경우 예외가 발생한다.")
	@Test
	void deductQuantityException() {
		// given
		Stock stock = Stock.create("001", 2);

		// when / then
		assertThatThrownBy(() -> stock.deductQuantity(3))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("차감할 재고 수량이 없습니다.");

	}
}
