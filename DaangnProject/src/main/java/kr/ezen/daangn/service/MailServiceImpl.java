package kr.ezen.daangn.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import kr.ezen.daangn.vo.DaangnMemberVO;
import lombok.extern.slf4j.Slf4j;

@Service(value = "mailService")
@Slf4j
public class MailServiceImpl implements MailService{
	
	@Autowired
	private JavaMailSender javaMailSender; // 메일을 보내기 위한 객체
	
	@Override
	public String mailSend(String to) {
		log.debug("mailsend({}) 실행", to);
		MailHandler mailHandler = null;
		String authCode= null;
		try {
			mailHandler = new MailHandler(javaMailSender);
			mailHandler.setFrom("tjdtlr12349@naver.com", "당근마켓");					// 누가
			mailHandler.setTo(to);										// 누구에게
			mailHandler.setSubject("당근마켓 인증번호");  				// 제목
			authCode = createCode();
			mailHandler.setText("인증번호: " + authCode); // 내용
			mailHandler.send();	// 전송
			log.debug("메일 전송 성공!!!!!!");
		} catch (MessagingException e) {
			log.debug("메일 전송 실패!!!!!!");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			log.debug("메일 전송 실패!!!!!!");
			e.printStackTrace();
		}
		return authCode;
	}
	
	// 인증 코드 생성 메서드
	private String createCode() {
		// 인증번호에 사용될 문자들
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		// 무작위로 생성된 인증번호를 담을 문자열 변수
		StringBuilder code = new StringBuilder();
		// Random 객체 생성
		Random random = new Random();
		// 6자리의 인증번호 생성
		for (int i = 0; i < 6; i++) {
			// characters 문자열 중 무작위 인덱스 선택
			int randomIndex = random.nextInt(characters.length());
			
			// 선택된 인덱스에 해당하는 문자를 인증번호 문자열에 추가
			code.append(characters.charAt(randomIndex));
		}
		// 생성된 인증번호 반환
		return code.toString();
	}
	
	@Autowired
	private DaangnMemberService daangnMemberService;
	
	@Override
	public Map<String, List<DaangnMemberVO>> adminMailSend(List<Integer> userIdxList, String title, String subject) {
		Map<String, List<DaangnMemberVO>> map = new HashMap<>();
		List<DaangnMemberVO> successList = new ArrayList<>();
	    List<DaangnMemberVO> failureList = new ArrayList<>();
		MailHandler mailHandler = null;
		for (Integer userIdx : userIdxList) {
	        String email = null; // 이메일 변수를 선언하고 초기화
	        try {
	            mailHandler = new MailHandler(javaMailSender);
	            
	            email = daangnMemberService.selectByIdx(userIdx).getEmail();
	            mailHandler.setFrom("tjdtlr12349@naver.com", "jungBlogCompany");
	            mailHandler.setTo(email);
	            mailHandler.setSubject(subject);
	            mailHandler.setText(subject);
	            log.info("메일 전송 중...");
	            mailHandler.send();
	            log.info("메일 전송 성공!!!!!!");
	            DaangnMemberVO memberVO = new DaangnMemberVO();
	            memberVO.setIdx(userIdx);
	            memberVO.setUsername(email);
	            successList.add(memberVO); // 성공한 경우에는 성공 리스트에 추가
	        } catch (MessagingException | UnsupportedEncodingException e) {
	            log.info("메일 전송 실패!!!!!!");
	            e.printStackTrace();
	            if (email != null) {
	            	DaangnMemberVO memberVO = new DaangnMemberVO();
		            memberVO.setIdx(userIdx);
		            memberVO.setUsername(email);
	                failureList.add(memberVO); // 실패한 경우에는 실패 리스트에 추가
	            }
	        }
	    }
		map.put("성공", successList);
		map.put("실패", failureList);
		return map;
	}
}
