package kr.ezen.daangn.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.DaangnLikeService;
import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.service.DaangnUserFileService;
import kr.ezen.daangn.service.MailService;
import kr.ezen.daangn.vo.DaangnFileVO;
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
	
	
	/** 로그인 주소 */
	@GetMapping(value = "/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model) {
		if (error != null)
			model.addAttribute("error", "error");
		if (logout != null)
			model.addAttribute("logout", "logout");
		return "login";
	}

	/** 회원가입 주소 */
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
	/** 로그아웃 */
	@GetMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		log.info("logout 실행");
		request.getSession().removeAttribute("user");
		request.getSession().invalidate();
		log.info("session.user =>{}", request.getSession().getAttribute("user"));
		return "redirect:/";
	}
	
	/** 회원가입중 필요한 메일을 보내는 주소 */
    @PostMapping(value = "/send", produces = "text/plain" )
    @ResponseBody
    public String send(@RequestBody HashMap<String, String> map) {
    	String to = map.get("to");
    	log.info("send : to=>{}",to);
    	String result = mailService.mailSend(to); // 인증번호!
    	log.info("send Success?:{}", result);
    	return result;
    }
    
    /** 회원 찾기중 email의 해당하는 username 주는 주소 */
    @PostMapping(value = "/findUserName")
    @ResponseBody
    public String findUserName(@RequestBody DaangnMemberVO memberVO) {
    	String result = null;
    	String userName = daangnMemberService.selectUserNameByEmail(memberVO.getEmail());
    	if( userName != null) {
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < userName.length(); i++) {
                if (2 <=i && i < 6) {
                    sb.append("*");
                } else {
                	if(10<=i && i <14) {
                		sb.append("*");
                	} else {
                		sb.append(userName.charAt(i));	                		
                	}
                }
            }
            result = sb.toString();
    	}
    	return result;
    }
    
    /** 회원 찾기중 username에 해당하는 email과 넘어온 email이 일치함을 확인하는 주소 */
    @PostMapping(value = "/checkEmailAndUsername")
    @ResponseBody
    public String checkEmailAndUsername(@RequestBody DaangnMemberVO memberVO) {
    	log.info("checkEmailAndUsername : {}", memberVO);
    	int result = 0;
    	String userName = daangnMemberService.selectUserNameByEmail(memberVO.getEmail());
    	log.info("username => {}", userName);
    	if(userName != null) {
    		if(memberVO.getUsername().equals(userName)) {
    			result = 1;
    		}
    	}
    	return result + "";
    }
    
    
    /** 마이페이지! */
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
    
    /** 현재 로그인한 유저의 비밀번호와 보낸 비밀번호가 일치하는지 확인하는 주소 */
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
    
    /** 로그인안한유저 비밀번호 바꾸기 */
    @PostMapping(value = "/passwordUpdateByUsername")
    @ResponseBody
    public String passwordUpdateByUsername(@RequestBody DaangnMemberVO memberVO) {
    	int result = 0 //daangnMemberService.checkPasswordMatch(user, password);
    	return result + "";
    }
    
    
    /** 업데이트를 실행할 주소 */
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
    
    @PostMapping(value = "/updateUserProfile")
    public String updateUserProfile(HttpSession session, HttpServletRequest request, @RequestPart(value = "file") MultipartFile file) {
    	log.info("updateUserProfile 실행");
    	if(session.getAttribute("user") == null) {
    		return "redirect:/";
    	}
    	DaangnMemberVO sessionUser = (DaangnMemberVO) session.getAttribute("user");
    	// 이전 파일 삭제!
    	daangnUserFileService.deleteByUserIdx(sessionUser.getIdx());
    	// file save
		String uploadPath = request.getServletContext().getRealPath("/upload/");
		// 파일 객체 생성
		File file2 = new File(uploadPath);
		// 폴더가 없다면 폴더를 생성해준다.
		if (!file2.exists()) {
			file2.mkdirs();
		}
		log.info("서버 실제 경로 : " + uploadPath);
		if (file != null && file.getSize() > 0) { // 파일이 넘어왔다면
			try {
				// 저장파일의 이름 중복을 피하기 위해 저장파일이름을 유일하게 만들어 준다.
				String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
				String originFileName = file.getOriginalFilename();
				// 파일 객체를 만들어 저장해 준다.
				File saveFile = new File(uploadPath, saveFileName);
				// 파일 복사
				FileCopyUtils.copy(file.getBytes(), saveFile); // 이걸로 저장을 시킨다.!
				DaangnFileVO fileVO = new DaangnFileVO();
				fileVO.setRef(sessionUser.getIdx());
				fileVO.setOriginFileName(originFileName);
				fileVO.setSaveFileName(saveFileName);
				daangnUserFileService.insert(fileVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	return "redirect:/member/home";
    }
    
    
    //=====================================================================================
    // 여기는 아직 보류
    //=====================================================================================
    /** 좋아요한 글 보여주는 */
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
