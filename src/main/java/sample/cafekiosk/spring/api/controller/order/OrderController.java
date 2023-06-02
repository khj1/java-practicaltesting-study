package sample.cafekiosk.spring.api.controller.order;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateRequest;

@RestController
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/api/v1/orders/new")
	public void createOrder(@RequestBody OrderCreateRequest request) {
		LocalDateTime registerDateTime = LocalDateTime.now();
		orderService.createOrder(request, registerDateTime);
	}
}
