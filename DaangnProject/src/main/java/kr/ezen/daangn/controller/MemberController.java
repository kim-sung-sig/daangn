package kr.ezen.daangn.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.service.MailService;
import kr.ezen.daangn.vo.DaangnMemberVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Configuration
@RequestMapping(value = "/member")
@Slf4j
public class MemberController {
	@Autowired
	private DaangnMemberService daangnMemberService;
	
	@GetMapping(value = "/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model) {
		if (error != null)
			model.addAttribute("error", "error");
		if (logout != null)
			model.addAttribute("logout", "logout");
		return "login";
	}

	// 회원가입 폼
	@GetMapping(value = "/join")
	public String join(HttpSession session) {
		if (session.getAttribute("user") != null) { // 나쁜사람 방지
			session.removeAttribute("user");
			session.invalidate();
			return "redirect:/";
		}
		return "join";
	}
	@GetMapping(value = "/login/useridcheck", produces = "text/plain;charset=UTF-8")
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
		daangnMemberService.insert(memberVO);
		return "redirect:/member/login";
	}
	
	@GetMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		log.info("logout 실행");
		request.getSession().removeAttribute("user");
		request.getSession().invalidate();
		log.info("session.user =>{}", request.getSession().getAttribute("user"));
		return "redirect:/";
	}
	
	@GetMapping(value = "/home")
	public String home(HttpServletRequest request, Model model) {
		return "home";
	}
	
	@Autowired
	private MailService mailService;
	
	// 회원가입중 필요한 이메일 인증을 보내는 주소
    @GetMapping(value = "/send", produces = "text/plain" )
    @ResponseBody
    public String send(@RequestParam(value = "to") String to) {
    	log.info("send : to=>{}",to);
    	String result = mailService.mailSend(to); // 인증번호!
    	log.info("send Success?:{}", result);
    	return result;
    }
}
