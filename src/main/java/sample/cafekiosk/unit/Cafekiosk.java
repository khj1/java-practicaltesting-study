package sample.cafekiosk.unit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

@Getter
public class Cafekiosk {

	private final List<Beverage> beverages = new ArrayList<>();

	public void add(final Beverage beverage) {
		beverages.add(beverage);
	}

	public void delete(Beverage beverage) {
		beverages.remove(beverage);
	}

	public void clear() {
		beverages.clear();
	}

	public int calculateTotalPrice() {
		return beverages.stream()
				.mapToInt(Beverage::getPrice)
				.sum();
	}

	public Order createOrder() {
		return new Order(LocalDateTime.now(), beverages);
	}
}
