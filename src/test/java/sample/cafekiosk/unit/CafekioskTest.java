package sample.cafekiosk.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import sample.cafekiosk.unit.beverage.Americano;

class CafekioskTest {

	@Test
	void add() {
		Cafekiosk cafekiosk = new Cafekiosk();
		cafekiosk.add(new Americano());

		System.out.println("담긴 음료의 수 = " + cafekiosk.getBeverages().size());
		System.out.println("담긴 음료의 이름 = " + cafekiosk.getBeverages().get(0).getName());
	}
}
