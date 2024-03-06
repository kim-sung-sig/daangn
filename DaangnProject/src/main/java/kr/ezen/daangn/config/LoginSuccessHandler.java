package kr.ezen.daangn.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.vo.DaangnMemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired
	private DaangnMemberService daangnMemberService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		log.info("로그인 성공" + authentication.getName());		
		log.info("로그인 성공" + authentication.getPrincipal());
		DaangnMemberVO user = (DaangnMemberVO) authentication.getPrincipal();
		daangnMemberService.updateLastLoginDate(user.getIdx());
		request.getSession().setAttribute("user", user); // 이제 유저를 세션에 올려준것!
		request.getSession().setMaxInactiveInterval(60*30); // 세션 시간 30분
		response.sendRedirect("/"); // 하고 위치로 보낸다! 만약여기서 원래 보던 곳으로 넘길수 있다면? 좋을듯?
	}
}
