package sample.cafekiosk.unit;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

@Getter
public class Cafekiosk {

	public static final LocalTime OPEN_HOUR = LocalTime.of(10, 0);
	public static final LocalTime CLOSE_HOUR = LocalTime.of(21, 0);

	private final List<Beverage> beverages = new ArrayList<>();

	public void add(final Beverage beverage) {
		beverages.add(beverage);
	}

	public void add(final Beverage beverage, int count) {
		if (count <= 0) {
			throw new IllegalArgumentException("음료는 한 잔 이상 주문하셔야 합니다.");
		}
		for (int i = 0; i < count; i++) {
			beverages.add(beverage);
		}
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
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalTime currentTime = currentDateTime.toLocalTime();
		if (currentTime.isBefore(OPEN_HOUR) || currentTime.isAfter(CLOSE_HOUR)) {
			throw new IllegalArgumentException("주문 시간이 아닙니다. 관리자에게 문의하세요.");
		}

		return new Order(currentDateTime, beverages);
	}

	public Order createOrder(LocalDateTime currentDateTime) {
		LocalTime currentTime = currentDateTime.toLocalTime();
		if (currentTime.isBefore(OPEN_HOUR) || currentTime.isAfter(CLOSE_HOUR)) {
			throw new IllegalArgumentException("주문 시간이 아닙니다. 관리자에게 문의하세요.");
		}

		return new Order(currentDateTime, beverages);
	}

}
