package sample.cafekiosk.spring.api.service.order;

import static sample.cafekiosk.spring.domain.order.OrderStatus.PAYMENT_COMPLETED;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.service.mail.MailService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;

@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

	private final OrderRepository orderRepository;
	private final MailService mailService;

	/** TODO 트랜잭션
	 * 이메일을 전송하는 것과 같이 외부 네트워크가 걸려서 한 사이클이 긴 작업의 경우
	 * @Transactional 어노테이션을 붙이지 않는 것이 좋다.
	 * - 트랜잭션이 DB 자원을 계속 물고 있기 때문에!
	 * - 이메일 전송 기능의 경우 History
	 *
	 * 조회 로직의 경우엔 DB에서 조회 트랜잭션이 따로 걸리기 때문에 괜찮다.
	 */
	public boolean sendOrderStatisticsMail(LocalDate orderDate, String email) {
		List<Order> orders = orderRepository.findOrdersBy(
				orderDate.atStartOfDay(),
				orderDate.plusDays(1).atStartOfDay(),
				PAYMENT_COMPLETED);

		int totalPrice = orders.stream()
				.mapToInt(Order::getTotalPrice)
				.sum();

		boolean result = mailService.sendMail(
				"no-reply@cafekiosk.com",
				email,
				String.format("[매출통계] %s", orderDate),
				String.format("총 매출 합계는 %d원 입니다.", totalPrice)
		);

		if (!result) {
			throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
		}

		return true;
	}
}
