package sample.cafekiosk.spring.api.service.mail;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.client.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

@RequiredArgsConstructor
@Service
public class MailService {

	private final MailSendClient mailSendClient;
	private final MailSendHistoryRepository mailSendHistoryRepository;

	public boolean sendMail(final String fromEmail, final String toEmail,
							final String subject, final String description) {

		boolean result = mailSendClient.sendEmail(fromEmail, toEmail, subject, description);
		if (result) {
			mailSendHistoryRepository.save(MailSendHistory.builder()
					.fromEmail(fromEmail)
					.toEmail(toEmail)
					.subject(subject)
					.description(description)
					.build()
			);
			return true;
		}
		return false;
	}
}
