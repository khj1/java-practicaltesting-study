package sample.cafekiosk.spring.api.service.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

/**
 * readOnly = true -> 읽기 전용
 * - CRUD 작업에서 CUD 동작 x / only Read 동작
 * - JAP: CUD 스냅샷 저장, 변경 감지 x (성능 향상)
 *
 * CQRS - Command / Query 분리
 * - Read Query에 부하가 발생했을 때 이 영향이 Command Query까지 미치지 않도록 한다.
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;

	/**
	 * 상품 등록의 경우에도 동시성 문제가 발생할 수 있다.
	 */
	@Transactional
	public ProductResponse createProduct(final ProductCreateRequest request) {
		String newProductNumber = createNewProductNumber();

		Product product = request.toEntity(newProductNumber);
		Product savedProduct = productRepository.save(product);

		return ProductResponse.of(savedProduct);
	}

	public List<ProductResponse> getSellingProducts() {
		List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

		return products.stream()
				.map(ProductResponse::of)
				.collect(Collectors.toList());
	}

	private String createNewProductNumber() {
		String latestProductNumber = productRepository.findLatestProductNumber();
		if (latestProductNumber == null) {
			return "001";
		}
		return String.format("%03d", Integer.parseInt(latestProductNumber) + 1);
	}

}
