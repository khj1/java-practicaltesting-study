package sample.cafekiosk.spring.api.service.order.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

	private List<String> productNumbers;

	@Builder
	public OrderCreateRequest(final List<String> productNumbers) {
		this.productNumbers = productNumbers;
	}
}
