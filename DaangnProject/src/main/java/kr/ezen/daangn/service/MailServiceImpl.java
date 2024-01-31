package kr.ezen.daangn.service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Service(value = "mailService")
@Slf4j
public class MailServiceImpl implements MailService{
	
	@Autowired
	private JavaMailSender javaMailSender; // 메일을 보내기 위한 객체
	
	@SuppressWarnings("null")
	@Override
	public String mailSend(String to) {
		log.debug("mailsend({}) 실행", to);
		MailHandler mailHandler = null;
		String result = "";
		StringBuffer key = new StringBuffer();
		try {
			mailHandler = new MailHandler(javaMailSender);
			mailHandler.setFrom("tjdtlr12349@naver.com", "당근마켓");					// 누가
			mailHandler.setTo(to);										// 누구에게
			mailHandler.setSubject("당근마켓 인증번호");  				// 제목
			Random random = new Random();
			for(int i=0; i<6; i++) {
				key.append(random.nextInt(10));				
			}
			mailHandler.setText(key.toString());						// 내용
			mailHandler.send();	// 전송
			result = key.toString();
			log.debug("메일 전송 성공!!!!!!");
		} catch (MessagingException e) {
			log.debug("메일 전송 실패!!!!!!");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			log.debug("메일 전송 실패!!!!!!");
			e.printStackTrace();
		}
		return result;
	}

}
