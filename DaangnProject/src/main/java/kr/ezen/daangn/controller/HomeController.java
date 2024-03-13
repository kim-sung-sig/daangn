package kr.ezen.daangn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.vo.DaangnMemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Configuration
public class HomeController {
	
	@Autowired
	private DaangnMemberService daangnMemberService;
	
	@GetMapping(value = { "/", "/main", "/index" })
	public String home(HttpSession session, Model model) {
		DaangnMemberVO user = (DaangnMemberVO) session.getAttribute("user");
		if(user != null) {
			if(!(Boolean) session.getAttribute("isLogin")) {
				daangnMemberService.updateLastLoginDate(user.getIdx());
				session.setAttribute("isLogin", true);
			}
			model.addAttribute("a", daangnMemberService.selectByIdx(user.getIdx()));
		}
		return "index";
	}
	//===========================================================================================
	
	// 딱! 한번만 실행해야한다!
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;	
	
	/**
	 * 초기 어드민들 패스워드 암호화하는 주소
	 * @return
	 */
	// @GetMapping("/dbinit") // 기존에 등록된 비번을 암호화 해서 변경한다. 1번만 실행하고 지워줘라~~~
	public String dbInit() {
		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"admin");
		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"master");
		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"webmaster");
		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"root");
		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"dba");
		return "redirect:/";
	}
	
	@GetMapping(value = "/asd")
	public String test() {
		return "test";
	}
	@GetMapping(value = "/qwe")
	public String test2() {
		return "test2";
	}
	
	@GetMapping(value = "/chatLeaveTest")
	@ResponseBody
	public String chatLeaveTest() {
		log.info("채팅방을 떠남을 알림");
		return "1";
	}
	
}
