package sample.cafekiosk.spring.api.service.product;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.domain.product.ProductRepository;

/**
 * private 메서드를 검증해야 될 것 같다는 생각이 들면
 * 그 때가 객체를 분리해야 타이밍이다.
 */
@RequiredArgsConstructor
@Component
public class ProductNumberFactory {

	private final ProductRepository productRepository;

	public String createNewProductNumber() {
		String latestProductNumber = productRepository.findLatestProductNumber();
		if (latestProductNumber == null) {
			return "001";
		}
		return String.format("%03d", Integer.parseInt(latestProductNumber) + 1);
	}
}
