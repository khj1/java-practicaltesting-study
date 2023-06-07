package sample.cafekiosk.spring.docs.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import sample.cafekiosk.spring.api.controller.product.ProductController;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.docs.RestDocsSupport;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

public class ProductControllerDocsTest extends RestDocsSupport {

	private final ProductService productService = mock(ProductService.class);

	@Override
	protected Object initController() {
		return new ProductController(productService);
	}

	@DisplayName("신규 상품 등록 API")
	@Test
	void createProduct() throws Exception {
		ProductCreateRequest request = ProductCreateRequest.builder()
				.type(HANDMADE)
				.name("아메리카노")
				.sellingStatus(SELLING)
				.price(4_000)
				.build();

		given(productService.createProduct(any(ProductCreateServiceRequest.class)))
				.willReturn(createProductResponse("001", 1_000, "아메리카노", SELLING, HANDMADE, 1L));

		mockMvc.perform(post("/api/v1/products/new")
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document("product/create",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("type").type(JsonFieldType.STRING).description("상품 타입"),
								fieldWithPath("sellingStatus").optional()
										.type(JsonFieldType.STRING)
										.description("상품 판매 상태"),
								fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
								fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격")
						),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
								fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
								fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
								fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("상품 ID"),
								fieldWithPath("data.productNumber").type(JsonFieldType.STRING).description("상품 번호"),
								fieldWithPath("data.type").type(JsonFieldType.STRING).description("상품 타입"),
								fieldWithPath("data.sellingStatus").type(JsonFieldType.STRING).description("상품 판매 상태"),
								fieldWithPath("data.name").type(JsonFieldType.STRING).description("상품 이름"),
								fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("상품 가격")
						)
				));
	}

	@DisplayName("")
	@Test
	void getSellingProducts() throws Exception {
		// given
		given(productService.getSellingProducts())
				.willReturn(List.of(
						createProductResponse("001", 1_000, "아메리카노", SELLING, HANDMADE, 1L),
						createProductResponse("002", 2_000, "카페라떼", SELLING, HANDMADE, 2L)
				));

		mockMvc.perform(get("/api/v1/products/selling"))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document("product/getSellingProducts",
						preprocessResponse(prettyPrint()),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
								fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
								fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
								fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("상품 ID"),
								fieldWithPath("data.productNumber").type(JsonFieldType.STRING).description("상품 번호"),
								fieldWithPath("data.type").type(JsonFieldType.STRING).description("상품 타입"),
								fieldWithPath("data.sellingStatus").type(JsonFieldType.STRING).description("상품 판매 상태"),
								fieldWithPath("data.name").type(JsonFieldType.STRING).description("상품 이름"),
								fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("상품 가격")
						)
				));

	}

	private ProductResponse createProductResponse(
			final String productNumber,
			final int price,
			final String name,
			final ProductSellingStatus sellingStatus,
			final ProductType type,
			final Long id
	) {
		return ProductResponse.builder()
				.productNumber(productNumber)
				.price(price)
				.name(name)
				.sellingStatus(sellingStatus)
				.type(type)
				.id(id)
				.build();
	}
}
