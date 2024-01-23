package kr.ezen.daangn.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.vo.DaangnMemberVO;

@Controller
public class HomeController {
	
	@Autowired
	private DaangnMainBoardService daangnMainBoardService;
	
	@Autowired
	private DaangnMemberService daangnMemberService;
	

	@GetMapping(value = { "/", "/main", "/index" })
	public String home() {
		return "index";
	}
	
	//===========================================================================================
	// 회원가입용 컨트롤러
	//-------------------------------------------------------------------------------------------
	@GetMapping(value = { "/login" })
	public String login(@RequestParam(value = "error", required = false) String error
						, @RequestParam(value = "logout", required = false) String logout
						, Model model) {
		if (error != null)
			model.addAttribute("error", "error");
		if (logout != null)
			model.addAttribute("logout", "logout");
		return "login";
	}
	//회원가입 폼
	@GetMapping(value = {"/join"})
	public String join(HttpSession session) {
		if(session.getAttribute("user") != null) { // 나쁜사람 방지
			session.removeAttribute("user");
			session.invalidate();
			return "redirect:/";
		}
		return "join";
	}
	@GetMapping(value = "/login/userIdCheck", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String userIdCheck(@RequestParam(value = "username") String username) {
		return daangnMemberService.selectCountByUsername(username)+""; // 1 or 0
	}
	@GetMapping(value = {"/joinok"})
	public String joinOkGet(HttpSession session) {
		if(session.getAttribute("user") != null) { // 나쁜사람 방지
			session.removeAttribute("user");
			session.invalidate();
		}
		return "redirect:/";
	}
	@PostMapping(value = {"/joinok"})
	public String joinOkPost(@ModelAttribute(value = "vo") DaangnMemberVO memberVO, @RequestParam(value = "email2") String email2, @RequestParam(value = "bd") String bd) {
		// 내용검증을 해야하지만! 넘긴다!
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(bd);
			memberVO.setBirthDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		memberVO.setEmail(memberVO.getEmail()+"@"+email2);
		daangnMemberService.insert(memberVO); // 저장
//		return "joinOk";
		return "redirect:/login";
	}
	//===========================================================================================
	
// 딱! 한번만 실행해야한다!
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
//	
//	@Autowired
//	private BCryptPasswordEncoder passwordEncoder;
	
//	@GetMapping("/dbinit") // 기존에 등록된 비번을 암호화 해서 변경한다. 1번만 실행하고 지워줘라~~~
//	public String dbInit() {
//		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"admin");
//		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"master");
//		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"webmaster");
//		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"root");
//		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"dba");
//		return "redirect:/";
//	}
		
}
