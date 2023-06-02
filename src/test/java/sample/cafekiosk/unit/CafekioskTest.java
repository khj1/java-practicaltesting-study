package sample.cafekiosk.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;

class CafekioskTest {

	@Test
	void add_manual_test() {
		Cafekiosk cafekiosk = new Cafekiosk();
		cafekiosk.add(new Americano());

		System.out.println("담긴 음료의 수 = " + cafekiosk.getBeverages().size());
		System.out.println("담긴 음료의 이름 = " + cafekiosk.getBeverages().get(0).getName());
	}

	@Test
	void add() {
		Cafekiosk cafekiosk = new Cafekiosk();
		cafekiosk.add(new Americano());

		assertThat(cafekiosk.getBeverages().size()).isEqualTo(1);
		assertThat(cafekiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
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
}
