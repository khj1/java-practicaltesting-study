package sample.cafekiosk.spring.api.controller.order;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;

@RestController
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	/**
	 * Layered Architecture를 설계할 때 가장 좋은 형태는
	 * 하위 레이어가 상위 레이어에 의존하지 않는 것이 가장 좋다.
	 * 따라서 controller 단에서 사용되는 OrderCreateRequest 를 Service 레이어까지 끌고가기 보다는
	 * OrderCreateServiceRequest 를 새로 정의해줘서 의존성을 끊어주는 것을 권장한다.
	 * - 이 경우 서비스와 컨트롤러의 모듈을 분리할 때 유용하다.
	 * - 서비스단에서 사용되는 Request 객체에서는 굳이 컨트롤러 단에서 검증된 내용을 재 검증할 필요가 없다.
	 * - 따라서 spring-starter validation 의존성 또한 Service 단과 분리될 수 있다.
	 */
	@PostMapping("/api/v1/orders/new")
	public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
		LocalDateTime registerDateTime = LocalDateTime.now();
		return ApiResponse.ok(orderService.createOrder(request.toServiceRequest(), registerDateTime));
	}

}
