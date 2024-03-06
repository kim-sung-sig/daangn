package kr.ezen.daangn.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.DaangnLikeService;
import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.service.MailService;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Configuration
@RequestMapping(value = "/member")
@Slf4j
public class MemberController {
	@Autowired
	private DaangnMemberService daangnMemberService;
	@Autowired
	private DaangnMainBoardService daangnMainBoardService;
	@Autowired
	private DaangnLikeService daangnLikeService;
	@Autowired
	private MailService mailService;
	
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

	@PostMapping(value = "/login/useridcheck", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String userIdCheck(@RequestBody Map<String, String> requestBody) {
		String username = requestBody.get("username");
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
	public String joinOkPost(@ModelAttribute(value = "memberVO") DaangnMemberVO memberVO) {
		log.info("joinOkPost 실행 {}",memberVO);
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
	
	// 회원가입중 필요한 이메일 인증을 보내는 주소
    @GetMapping(value = "/send", produces = "text/plain" )
    @ResponseBody
    public String send(@RequestParam(value = "to") String to) {
    	log.info("send : to=>{}",to);
    	String result = mailService.mailSend(to); // 인증번호!
    	log.info("send Success?:{}", result);
    	return result;
    }
    
    
    @GetMapping(value = "/home")
	public String home(HttpSession session, Model model) {
    	if(session.getAttribute("user") == null) {
    		return "redirect:/";
    	}
    	log.info("home 실행");
    	DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
    	// 0. 유저 프로필 사진 넣어주기
    	
    	model.addAttribute("user", memberVO);
    	
    	// 3. 프로필보기(여기서 프로필수정 및 비번변경 가능 탈퇴)
    	// 4. ++ 구매내역?
		return "home";
	}
    
    @GetMapping(value = "/home/Like")
    @SuppressWarnings("null")
    public String homeLike(HttpSession session, Model model) {
    	if(session.getAttribute("user") == null) {
    		return "redirect:/";
    	}
    	log.info("homeLike 실행");
    	
    	DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
    	// 1. 유저가 좋아요한 목록들 (관심목록)
    	List<DaangnMainBoardVO> boardList = null;
    	List<Integer> likeList = daangnLikeService.selectLikeByUseridx(memberVO.getIdx());
    	for(Integer i : likeList) {
    		DaangnMainBoardVO boardVO = daangnMainBoardService.selectByIdx(i);
    		if(boardVO != null) {
    			boardList.add(boardVO);    			
    		}
    	}
    	model.addAttribute("boardList", boardList); // 관심목록 넘겨주기!
    	return "homeLike";
    }
    
    @GetMapping(value = "/home/sell")
    public String homeSell(HttpSession session, Model model) {
    	if(session.getAttribute("user") == null) {
    		return "redirect:/";
    	}
    	log.info("homeSell 실행");
    	DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
    	// 2. 유저가 쓴글 (판매내역)
    	List<DaangnMainBoardVO> boardList = daangnMainBoardService.selectByUserIdx(memberVO.getIdx()); // 사진도 넘겨주나?
    	model.addAttribute("boardList", boardList); // 관심목록 넘겨주기!
    	return "homeSell";
    }
}
