package sample.cafekiosk.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

/**
 * 테스트는 문서다.
 * - 자신이 테스트 코드를 짜며 했던 고민과 그 결과물을 팀 차원에서 공유할 수 있어야 한다.
 * - DisplayName은 명사의 나열보다 문장으로 작성하는 것이 좋다.
 * - 테스트 행위에 대한 결과까지 기술하면 더 좋다.
 * - 도메인 용어를 사용하여 한층 추상화된 내용을 담자.
 * 	 - 특정 시간x, 영업 시간o
 * 	 - 도메인 용어란 해당 도메인에서 사용되는 단어를 의미한다.
 *
 * BDD, Behavior Driven Development
 * - TDD에서 파생된 개발 방법(given / when / then)
 * - 함수 단위가 아닌 시나리오에 기반한 테스트 케이스에 집중한다.
 * - 개발자가 아닌 사람이 봐도 이해할 수 있을 정도의 추상화 수준을 권장한다.
 */
class CafekioskTest {

	@Test
	void add_manual_test() {
		Cafekiosk cafekiosk = new Cafekiosk();
		cafekiosk.add(new Americano());

		System.out.println("담긴 음료의 수 = " + cafekiosk.getBeverages().size());
		System.out.println("담긴 음료의 이름 = " + cafekiosk.getBeverages().get(0).getName());
	}

	@DisplayName("음료 1개를 추가하면 주문 목록에 담긴다.")
	@Test
	void add() {
		Cafekiosk cafekiosk = new Cafekiosk();
		cafekiosk.add(new Americano());

		assertThat(cafekiosk.getBeverages().size()).isEqualTo(1);
		assertThat(cafekiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
	}

	@Test
	void addSeveralBeverage() {
		Cafekiosk cafekiosk = new Cafekiosk();
		Americano americano = new Americano();

		cafekiosk.add(americano, 2);

		assertThat(cafekiosk.getBeverages()).hasSize(2);
		assertThat(cafekiosk.getBeverages()).containsExactly(americano, americano);
	}

	@Test
	void addZeroBeverage() {
		Cafekiosk cafekiosk = new Cafekiosk();
		Americano americano = new Americano();

		assertThatThrownBy(() -> cafekiosk.add(americano, 0))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("음료는 한 잔 이상 주문하셔야 합니다.");
	}

	@Test
	void remove() {
		Cafekiosk cafekiosk = new Cafekiosk();
		Americano americano = new Americano();

		cafekiosk.add(americano);
		assertThat(cafekiosk.getBeverages()).hasSize(1);

		cafekiosk.delete(americano);
		assertThat(cafekiosk.getBeverages()).isEmpty();
	}

	@Test
	void clear() {
		Cafekiosk cafekiosk = new Cafekiosk();

		cafekiosk.add(new Americano());
		cafekiosk.add(new Latte());
		assertThat(cafekiosk.getBeverages()).hasSize(2);

		cafekiosk.clear();
		assertThat(cafekiosk.getBeverages()).isEmpty();
	}

	@DisplayName("주문 목록에 담긴 상품들의 총 금액을 계산할 수 있다.")
	@Test
	void calculateTotalPrice() {
		// given
		Cafekiosk cafekiosk = new Cafekiosk();
		Americano americano = new Americano();
		Latte latte = new Latte();

		cafekiosk.add(americano);
		cafekiosk.add(latte);

		// when
		int totalPrice = cafekiosk.calculateTotalPrice();

		// then
		assertThat(totalPrice).isEqualTo(8_500);
	}

	@Test
	void createOrder() {
		Cafekiosk cafekiosk = new Cafekiosk();
		Americano americano = new Americano();
		cafekiosk.add(americano);

		Order order = cafekiosk.createOrder();

		assertThat(order.getBeverages()).hasSize(1);
		assertThat(order.getBeverages().get(0)).isEqualTo(americano);
	}

	@Test
	void createOrderCurrentTime() {
		Cafekiosk cafekiosk = new Cafekiosk();
		Americano americano = new Americano();
		cafekiosk.add(americano);

		Order order = cafekiosk.createOrder(LocalDateTime.of(2023, 6, 2, 11, 7));

		assertThat(order.getBeverages()).hasSize(1);
		assertThat(order.getBeverages().get(0)).isEqualTo(americano);
	}

	@Test
	void createOrderOutsideOpenTime() {
		Cafekiosk cafekiosk = new Cafekiosk();
		Americano americano = new Americano();
		cafekiosk.add(americano);

		assertThatThrownBy(() -> cafekiosk.createOrder(LocalDateTime.of(2023, 6, 2, 22, 7)))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요.");
	}
}
