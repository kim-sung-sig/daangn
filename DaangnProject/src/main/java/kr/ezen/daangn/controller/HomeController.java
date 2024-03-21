package kr.ezen.daangn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.service.DaangnNoticesService;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import kr.ezen.daangn.vo.NoticesVO;
import kr.ezen.daangn.vo.PagingVO;

@Controller
@Configuration
public class HomeController {
	
	@Autowired
	private DaangnMemberService daangnMemberService;
	@Autowired
	private DaangnNoticesService noticesService;
	
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
	// 공지사항 목록
	//===========================================================================================
	/** 공지사항 목록보기 페이지 */
	@GetMapping(value = "/notice")
	public String notice(Model model, @ModelAttribute CommonVO cv) {
	    cv.setS(10);
	    cv.setB(5);
	    PagingVO<NoticesVO> noticeList = noticesService.getPagingList(cv);
	    model.addAttribute("pv", noticeList);
	    return "notices";
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
	//@GetMapping("/dbinit") // 기존에 등록된 비번을 암호화 해서 변경한다. 1번만 실행하고 지워줘라~~~
	public String dbInit() {
		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"admin");
		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"master");
		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"webmaster");
		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"root");
		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"dba");
		return "redirect:/";
	}
}
