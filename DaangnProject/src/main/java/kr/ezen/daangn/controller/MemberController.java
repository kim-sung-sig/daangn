package kr.ezen.daangn.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.DaangnLikeService;
import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.service.DaangnUserFileService;
import kr.ezen.daangn.service.MailService;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Configuration
@RequestMapping(value = "/member")
public class MemberController {
	@Autowired
	private DaangnMemberService daangnMemberService;
	@Autowired
	private DaangnMainBoardService daangnMainBoardService;
	@Autowired
	private DaangnLikeService daangnLikeService;
	@Autowired
	private MailService mailService;
	@Autowired
	private DaangnUserFileService daangnUserFileService;
	
	
	/**
	 * 로그인 주소
	 */
	@GetMapping(value = "/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model) {
		if (error != null)
			model.addAttribute("error", "error");
		if (logout != null)
			model.addAttribute("logout", "logout");
		return "login";
	}

	/**
	 * 회원가입 주소
	 */
	@GetMapping(value = "/join")
	public String join(HttpSession session) {
		if (session.getAttribute("user") != null) { // 나쁜사람 방지
			session.removeAttribute("user");
			session.invalidate();
			return "redirect:/";
		}
		return "join";
	}
	
	/** 아이디 중복체크 확인하는 주소 */
	@PostMapping(value = "/login/useridcheck", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String userIdCheck(@RequestBody DaangnMemberVO memberVO) {
		String username = memberVO.getUsername();
		return daangnMemberService.selectCountByUsername(username)+""; // 1 or 0
	}
	/** 닉네임 중복체크 확인하는 주소 */
	@PostMapping(value = "/login/userNickNamecheck", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String userNickNameCheck(@RequestBody DaangnMemberVO memberVO) {
		String nickName = memberVO.getNickName();
		return daangnMemberService.selectCountByNickName(nickName)+""; // 1 or 0
	}
	
	// 회원 가입 ok
	@GetMapping(value = {"/joinok"})
	public String joinOkGet(HttpSession session) {
		if(session.getAttribute("user") != null) { // 나쁜사람 방지
			session.removeAttribute("user");
			session.invalidate();
		}
		return "redirect:/";
	}
	
	/**회원가입한 유저를 저장하는 주소*/
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
	
	/**회원가입중 필요한 메일을 보내는 주소*/
    @PostMapping(value = "/send", produces = "text/plain" )
    @ResponseBody
    public String send(@RequestBody HashMap<String, String> map) {
    	String to = map.get("to");
    	log.info("send : to=>{}",to);
    	String result = mailService.mailSend(to); // 인증번호!
    	log.info("send Success?:{}", result);
    	return result;
    }
    
    
    /**마이페이지!*/
    @GetMapping(value = "/home")
	public String home(HttpSession session, Model model) {
    	if(session.getAttribute("user") == null) {
    		return "redirect:/";
    	}
    	log.info("home 실행");
    	model.addAttribute("isMyPage", " ");
    	DaangnMemberVO sessionUser = (DaangnMemberVO) session.getAttribute("user");
    	DaangnMemberVO user = daangnMemberService.selectByIdx(sessionUser.getIdx());
    	model.addAttribute("user", user);
		return "myHome";
	}
    
    /**현재 로그인한 유저의 비밀번호와 보낸 비밀번호가 일치하는지 확인하는 주소*/
    @PostMapping(value = "/checkPasswordMatch")
    @ResponseBody
    public String checkPasswordMatch(HttpSession session, @RequestBody DaangnMemberVO memberVO) {
    	if(session.getAttribute("user") == null) {
    		return "0";
    	}
    	log.info("도달");
    	DaangnMemberVO sessionUser = (DaangnMemberVO) session.getAttribute("user");
    	DaangnMemberVO user = daangnMemberService.selectByIdx(sessionUser.getIdx());
    	String password = memberVO.getPassword();
    	int result = daangnMemberService.checkPasswordMatch(user, password);
    	return result + "";
    }
    @PutMapping(value = "/update")
    @ResponseBody
    public String update(HttpSession session, @RequestBody DaangnMemberVO memberVO) {
    	if(session.getAttribute("user") == null) {
    		return "0";
    	}
    	DaangnMemberVO sessionUser = (DaangnMemberVO) session.getAttribute("user");
    	memberVO.setIdx(sessionUser.getIdx());
    	log.info("userUpdate 실행 {}", memberVO);
    	int result = daangnMemberService.update(memberVO);
    	if(result == 1) {
    		DaangnMemberVO updatedUser = daangnMemberService.selectByIdx(sessionUser.getIdx());
    		session.setAttribute("user", updatedUser);
    	}
    	return result + "";
    }
    
    
    //=====================================================================================
    // 여기는 아직 보류
    //=====================================================================================
    /**
     * 좋아요한 글 보여주는?
     */
    @GetMapping(value = "/home/Like")
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
