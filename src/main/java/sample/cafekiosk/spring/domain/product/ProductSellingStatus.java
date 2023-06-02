package sample.cafekiosk.spring.domain.product;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductSellingStatus {

	SELLING("판매중"),
	HOLD("판매보류"),
	STOP_SELLING("판매중지");

	private final String text;

	public static List<ProductSellingStatus> forDisplay() {
		return List.of(SELLING, HOLD);
	}
}
