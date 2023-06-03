package sample.cafekiosk.spring.api.controller.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

	@NotNull(message = "상품 타입은 필수입니다.")
	private ProductType type;

	@NotNull(message = "상품 판매 상태는 필수입니다.")
	private ProductSellingStatus sellingStatus;

	/**
	 * @NotBlank -> 반드시 문자가 포함되어야 한다.
	 * @NotNull -> "", " "는 통과가 된다. (빈값, 공백은 통과가 된다.)
	 * @NotEmpty -> " "는 통과가 된다.
	 *
	 * 만약 상품 이름 글자 제한이 20 글자라면 @Max 어노테이션을 활용하면된다.
	 * 다만 상품 이름 글자 제한과 같은 특수한 경우의 검증을 반드시 Controller 단에서 해야하는지
	 * 의문을 가져 볼 필요가 있다.
	 *
	 * Controller 단에서는 좀 더 근본적인 검증을 실행하고,
	 * Service 단에서 특수하고 지엽적인 검증을 실행하는게 좋다.
	 */
	@NotBlank(message = "상품 이름은 필수입니다.")
	private String name;

	@Positive(message = "상품 가격은 양수여야 합니다.")
	private int price;

	@Builder
	public ProductCreateRequest(final ProductType type, final ProductSellingStatus sellingStatus, final String name,
								final int price) {
		this.type = type;
		this.sellingStatus = sellingStatus;
		this.name = name;
		this.price = price;
	}

	public ProductCreateServiceRequest toServiceRequest() {
		return ProductCreateServiceRequest.builder()
				.type(type)
				.sellingStatus(sellingStatus)
				.name(name)
				.price(price)
				.build();
	}
}
