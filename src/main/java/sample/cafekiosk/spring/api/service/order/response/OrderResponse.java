package sample.cafekiosk.spring.api.service.order.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.order.Order;

@Getter
public class OrderResponse {

	private Long id;
	private int totalPrice;
	private LocalDateTime registerDateTime;
	private List<ProductResponse> products;

	@Builder
	private OrderResponse(final Long id, final int totalPrice, final LocalDateTime registerDateTime,
						  final List<ProductResponse> products) {
		this.id = id;
		this.totalPrice = totalPrice;
		this.registerDateTime = registerDateTime;
		this.products = products;
	}

	public static OrderResponse of(final Order order) {
		return OrderResponse.builder()
				.id(order.getId())
				.totalPrice(order.getTotalPrice())
				.registerDateTime(order.getRegisterDateTime())
				.products(
						order.getOrderProducts().stream()
								.map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
								.collect(Collectors.toList())
				)
				.build();
	}
}
