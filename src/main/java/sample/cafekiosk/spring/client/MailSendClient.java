package sample.cafekiosk.spring.client;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MailSendClient {

	/**
	 * 실질적으로 메일 전송이 발생하는 지점
	 */
	public boolean sendEmail(final String fromEmail, final String toEmail, final String subject,
							 final String description) {
		log.info("메일 전송");
		return true;
	}
}
