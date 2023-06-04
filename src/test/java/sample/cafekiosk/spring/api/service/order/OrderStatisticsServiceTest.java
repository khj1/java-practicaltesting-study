package sample.cafekiosk.spring.api.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static sample.cafekiosk.spring.domain.order.OrderStatus.INIT;
import static sample.cafekiosk.spring.domain.order.OrderStatus.PAYMENT_COMPLETED;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import sample.cafekiosk.spring.client.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

/**
 * 메일 전송 기능 같은 경우 매 테스트마다 진짜 메일을 전송하면
 * 시간적으로나 리소스적으로나 낭비가 될 것이다. 따라서 이런 기능들은 Mocking 을 통해 테스트해주면 된다.
 *
 * Stub vs Mock
 * - 둘 다 특정한 메서드에 대한 결과를 명세하고
 * - Stub: 상태 검증
 * - Mock: 행위 검증
 */
@Transactional
@SpringBootTest
class OrderStatisticsServiceTest {

	@Autowired
	private OrderStatisticsService orderStatisticsService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private MailSendHistoryRepository mailSendHistoryRepository;

	@MockBean
	private MailSendClient mailSendClient;

	@DisplayName("결제 완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
	@Test
	void sendOrderStatisticsMail() {
		// given
		Product productA = createProduct("001", HANDMADE, "아메리카노", 1_000, SELLING);
		Product productB = createProduct("002", HANDMADE, "카푸치노", 2_000, SELLING);
		Product productC = createProduct("003", HANDMADE, "카페라떼", 3_000, SELLING);
		productRepository.saveAll(List.of(productA, productB, productC));

		Order orderA = createOrder(productA, productB, PAYMENT_COMPLETED, 4, 12, 30);
		Order orderB = createOrder(productA, productC, PAYMENT_COMPLETED, 5, 13, 0);
		Order orderC = createOrder(productB, productC, INIT, 4, 12, 0);
		orderRepository.saveAll(List.of(orderA, orderB, orderC));

		LocalDate orderDate = LocalDate.of(2023, 6, 4);

		// stubbing
		when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
				.thenReturn(true);

		// when
		boolean result = orderStatisticsService.sendOrderStatisticsMail(orderDate, "test@email.com");

		//then
		assertThat(result).isTrue();

		List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
		assertThat(histories).hasSize(1)
				.extracting("description")
				.contains("총 매출 합계는 3000원 입니다.");
	}

	private Product createProduct(final String productNumber, final ProductType type, final String name,
								  final int price, final ProductSellingStatus sellingStatus) {
		return Product.builder()
				.productNumber(productNumber)
				.type(type)
				.sellingStatus(sellingStatus)
				.name(name)
				.price(price)
				.build();
	}

	private Order createOrder(final Product productA, final Product productB, final OrderStatus paymentCompleted,
							  final int dayOfMonth, final int hour, final int minute) {
		return Order.create(List.of(productA, productB), paymentCompleted,
				LocalDateTime.of(2023, 6, dayOfMonth, hour, minute));
	}

}
