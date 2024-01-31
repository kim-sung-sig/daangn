package kr.ezen.daangn.service;

import java.io.UnsupportedEncodingException;

import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class MailHandler {
	private JavaMailSender javaMailSender;
	private MimeMessage message;
	private MimeMessageHelper messageHelper;

	// MailHandler의 생성자
	public MailHandler(JavaMailSender javaMailSender) throws MessagingException {
		this.javaMailSender = javaMailSender;
		message = javaMailSender.createMimeMessage();
		messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		// MimeMessageHelper의 생성자 두번째 파라미터는 다수의 사람에게 보낼 수 있는 설정, 세번째는 기본 인코딩 방식
	}

	// 이 이메일이 누구로부터 가는가.. 실제로 써본결과 그다지 중요하지 않은듯..잘 모르겠습니다.
	public void setFrom(@NonNull String email, @NonNull String name) throws UnsupportedEncodingException, MessagingException {
		messageHelper.setFrom(email, name);
	}

	// 누구에게 보낼 것인가.. 보낼사람의 이메일
	public void setTo(@NonNull String email) throws MessagingException {
		messageHelper.setTo(email);
	}

	// 보낼때 제목
	public void setSubject(@NonNull String subject) throws MessagingException {
		messageHelper.setSubject(subject);
	}

	// 보낼 메일의 내용.. 두번째 파라미터는 html을 적용할 것인가 아닌가. true시 html형식으로 작성하면 html형식으로 보임..
	public void setText(@NonNull String text) throws MessagingException {
		messageHelper.setText(text, true);
	}

	// 실제로 메일을 보내는 메서드..
	public void send() {
		try {
			javaMailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
