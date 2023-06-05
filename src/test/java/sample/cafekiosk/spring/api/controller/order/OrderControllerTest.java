package sample.cafekiosk.spring.api.controller.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import sample.cafekiosk.spring.ControllerSupportTest;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;

class OrderControllerTest extends ControllerSupportTest {

	@DisplayName("주문을 생성할 수 있다.")
	@Test
	void createOrder() throws Exception {
		// given
		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
				.productNumbers(List.of("001", "002"))
				.build();

		// when / then
		mockMvc.perform(post("/api/v1/orders/new")
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(jsonPath("$.status").value("OK"))
				.andExpect(jsonPath("$.message").value("OK"))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("주문 생성 시 상품 번호는 필수다.")
	@Test
	void createOrderWithoutProductNumbers() throws Exception {
		// given
		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
				.productNumbers(List.of())
				.build();

		// when / then
		mockMvc.perform(post("/api/v1/orders/new")
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("400"))
				.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.message").value("상품 번호는 필수입니다."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

}
