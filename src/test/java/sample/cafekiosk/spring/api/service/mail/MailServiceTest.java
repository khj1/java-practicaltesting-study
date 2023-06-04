package sample.cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import sample.cafekiosk.spring.client.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

/**
 * 스프링과 관련된 통합 테스트에서 Mock 을 사용해야 한다면 @MockBean 이 유용하다.
 * 그렇다면 순수한 단위 테스트에서 Mock 이 필요하다면 어떻게 풀어내야 할까?
 *
 * @ExtendWith(MockitoExtension.class)
 * - @SpringBootTest 를 사용하지 않기 때문에 Mockito를 사용할거야! 라고 선언해주는 작업이 필요하다.
 *
 * @Spy
 * - 만약 MailService 객체에 메서드가 5개 정도 있다고 가정해보자.
 * - 특정한 A 메서드만 Stubbing 하고 나머지 메서드는 실제 기능 그대로 사용하고 싶을 때가 있다.
 * - 이럴때 @Spy 가 굉장히 유용하다.
 * - @Spy 는 실제 객체를 기반으로 만들어지기 때문에 Mocito의 when() 메서드를 사용할 수 없다.
 * 	- 대신 doReturn(), doNothing() 등의 do() 메서드를 활용하면 된다.
 */
@ExtendWith(MockitoExtension.class)
class MailServiceTest {

	@Spy
	private MailSendClient mailSendClient;

	@Mock
	private MailSendHistoryRepository mailSendHistoryRepository;

	@InjectMocks
	private MailService mailService;

	@DisplayName("메일을 전송한다.")
	@Test
	void sendMail() {
		// given
		// when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
		// 		.thenReturn(true);

		doReturn(true)
				.when(mailSendClient)
				.sendEmail(anyString(), anyString(), anyString(), anyString());

		// when
		boolean result = mailService.sendMail("", "", "", "");

		//then
		assertThat(result).isTrue();

		// save() 메서드가 1번 호출됐는지 검증 (Spy가 하는 기능과 유사하다)
		verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));

	}
}
