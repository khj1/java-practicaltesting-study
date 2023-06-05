package sample.cafekiosk.spring.api.controller.product;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import sample.cafekiosk.spring.ControllerSupportTest;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

/**
 * @SpringBootTest 가 전체 에플리케이션 빈을 관장한다면
 * @WebMvcTest 는 좀 더 Presentation Layer와 관련된 빈들만 올린다.
 */
class ProductControllerTest extends ControllerSupportTest {

	@DisplayName("신규 상품을 등록한다.")
	@Test
	void createProduct() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
				.type(HANDMADE)
				.name("아메리카노")
				.sellingStatus(SELLING)
				.price(4_000)
				.build();

		// when / then
		mockMvc.perform(post("/api/v1/products/new")
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk());
	}

	@DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
	@Test
	void createProductWithoutType() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
				.type(null)
				.name("아메리카노")
				.sellingStatus(SELLING)
				.price(4_000)
				.build();

		// when / then
		mockMvc.perform(post("/api/v1/products/new")
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("400"))
				.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("신규 상품을 등록할 때 상품 판매 상태는 필수값이다.")
	@Test
	void createProductWithoutSellingStatus() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
				.type(HANDMADE)
				.name("아메리카노")
				.sellingStatus(null)
				.price(4_000)
				.build();

		// when / then
		mockMvc.perform(post("/api/v1/products/new")
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("400"))
				.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.message").value("상품 판매 상태는 필수입니다."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("신규 상품을 등록할 때 상품 이름은 필수값이다.")
	@Test
	void createProductWithoutName() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
				.type(HANDMADE)
				.name(null)
				.sellingStatus(SELLING)
				.price(4_000)
				.build();

		// when / then
		mockMvc.perform(post("/api/v1/products/new")
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("400"))
				.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("신규 상품을 등록할 때 가격은 양수여야 합니다.")
	@Test
	void createProductWithZeroPrice() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
				.type(HANDMADE)
				.name("아메리카노")
				.sellingStatus(SELLING)
				.price(0)
				.build();

		// when / then
		mockMvc.perform(post("/api/v1/products/new")
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("400"))
				.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("판매 상품을 조회한다.")
	@Test
	void getSellingProducts() throws Exception {
		// given
		List<ProductResponse> result = List.of();
		when(productService.getSellingProducts()).thenReturn(result);

		// when / then
		mockMvc.perform(
						get("/api/v1/products/selling")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(jsonPath("$.status").value("OK"))
				.andExpect(jsonPath("$.message").value("OK"))
				.andExpect(jsonPath("$.data").isArray());

	}
}
