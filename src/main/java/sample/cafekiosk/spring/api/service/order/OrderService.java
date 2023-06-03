package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final StockRepository stockRepository;

	private static Map<String, Long> createCountingMapBy(final List<String> stockProductNumbers) {
		return stockProductNumbers.stream()
				.collect(Collectors.groupingBy(p -> p, Collectors.counting()));
	}

	/**
	 * 재고 감소 -> 동시성 고민
	 * optimistic lock / pessimistic lock
	 */
	public OrderResponse createOrder(final OrderCreateRequest request, final LocalDateTime registerDateTime) {
		List<String> productNumbers = request.getProductNumbers();

		// Product
		List<Product> products = findProductsBy(productNumbers);

		// Stock
		deductStockQuantities(products);

		// Order
		Order order = Order.create(products, registerDateTime);
		Order savedOrder = orderRepository.save(order);

		return OrderResponse.of(savedOrder);
	}

	private void deductStockQuantities(final List<Product> products) {
		// 재고 차감이 필요한 상품들 필터링
		List<String> stockProductNumbers = extractStockProductNumbers(products);

		// 재고 Entity 조회
		Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);

		// 상품별 개수 세기
		Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

		// 재고 차감 시도
		for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
			Stock stock = stockMap.get(stockProductNumber);
			int quantity = productCountingMap.get(stockProductNumber).intValue();

			if (stock.isQuantityLessThan(quantity)) {
				throw new IllegalArgumentException("재고가 부족합니다.");
			}
			stock.deductQuantity(quantity);
		}
	}

	private List<Product> findProductsBy(final List<String> productNumbers) {
		List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
		Map<String, Product> productMap = products.stream()
				.collect(Collectors.toMap(Product::getProductNumber, p -> p));

		return productNumbers.stream()
				.map(productMap::get)
				.toList();
	}

	private List<String> extractStockProductNumbers(final List<Product> products) {
		return products.stream()
				.filter(product -> ProductType.containsStockType(product.getType()))
				.map(Product::getProductNumber)
				.toList();
	}

	private Map<String, Stock> createStockMapBy(final List<String> stockProductNumbers) {
		List<Stock> stocks = stockRepository.findByProductNumberIn(stockProductNumbers);

		return stocks.stream()
				.collect(Collectors.toMap(Stock::getProductNumber, s -> s));
	}
}
