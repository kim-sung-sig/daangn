package kr.ezen.daangn.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.service.DaangnNoticesService;
import kr.ezen.daangn.service.MailService;
import kr.ezen.daangn.service.PopularService;
import kr.ezen.daangn.service.VisitService;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import kr.ezen.daangn.vo.NoticesVO;
import kr.ezen.daangn.vo.PagingVO;
import kr.ezen.daangn.vo.PopularVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/adm")
public class AdminController {
	
	@Autowired
	private DaangnMemberService daangnMemberService;
	@Autowired
	private DaangnMainBoardService daangnMainBoardService;
	@Autowired
	private MailService mailService;
	@Autowired
	private PopularService popularService;
	@Autowired
	private VisitService visitService;
	@Autowired
	private DaangnNoticesService daangnNoticesService;
	
	//=========================================================================================================================================
	// 관리자 메인페이지
	@GetMapping(value = {"","/"})
	public String man(HttpSession session, Model model) {
		if(session.getAttribute("user") == null) {
    		return "redirect:/";
    	}
    	DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
    	if(!memberVO.getRole().equals("ROLE_ADMIN")) {
    		return "redirect:/";
    	}
    	log.info("관리자 홈 실행");
    	// 1. 회원수 및 회원목록 한 5개 정도만?
    	PagingVO<DaangnMemberVO> userpv = daangnMemberService.getUsers(new CommonVO());
    	List<DaangnMemberVO> memberList = userpv.getList().stream().limit(10).toList();
    	// 2. 최근 게시물 한 10개 정도만?
    	PagingVO<DaangnMainBoardVO> pv = daangnMainBoardService.selectList(new CommonVO()); // 10개 리스트
    	List<DaangnMainBoardVO> boardList = pv.getList().stream().limit(12).toList();
    	model.addAttribute("name",memberVO.getName());
    	model.addAttribute("users", memberList);
    	model.addAttribute("boards", boardList);
		return "admin/admin"; // 관리자 메인페이지
	}
	//=========================================================================================================================================
	// **회원관리**
	//=========================================================================================================================================
	// 회원관리페이지
	@GetMapping(value = "/userManagement")
	public String userManagement(@ModelAttribute(value = "cv") CommonVO cv, HttpSession session, Model model) {
		if(session.getAttribute("user") == null) {
    		return "redirect:/";
    	}
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
    	if(!memberVO.getRole().equals("ROLE_ADMIN")) {
    		return "redirect:/";
    	}
		PagingVO<DaangnMemberVO> pv = daangnMemberService.getUsers(cv);
		model.addAttribute("name",memberVO.getName());
		model.addAttribute("pv", pv);
		model.addAttribute("cv", cv);
		return "admin/userManagement"; // 관리자 메인페이지
	}
	
	//=========================================================================================================================================
	// 누구에게 메일을 전송할지 고르는 곳
	@GetMapping(value = "/mailToUser")
	public String mailToUser(HttpSession session, Model model) {
		if(session.getAttribute("user") == null) {
    		return "redirect:/";
    	}
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
    	if(!memberVO.getRole().equals("ROLE_ADMIN")) {
    		return "redirect:/";
    	}
    	
		model.addAttribute("name",memberVO.getName());
		return "admin/mailToUser"; // 관리자 메일선택창
	}
	
	
	@PostMapping(value = "/pagedUsers")
	@ResponseBody
	public List<DaangnMemberVO> pagingUser(@RequestBody Map<String, String> map){
		CommonVO cv = new CommonVO();
		cv.setP(Integer.parseInt(map.get("currentPage")));
		cv.setSearch(map.get("search"));
		cv.setS(20);
		cv.setB(5);
		cv.setEmailOk("0");
		PagingVO<DaangnMemberVO> pv = daangnMemberService.getUsers(cv);
		return pv.getList();
	}

	@PostMapping(value = "/getTotalCount")
	@ResponseBody
	public int getTotalCount(@RequestBody Map<String, Object> map){
		CommonVO cv = new CommonVO();
		cv.setSearch((String) map.get("search"));
		cv.setS(20);
		cv.setB(5);
		cv.setEmailOk("0");
		PagingVO<DaangnMemberVO> pv = daangnMemberService.getUsers(cv);
		return pv.getTotalCount();
	}
	
	
	// 누구에게가 정해진후 메일을 쓰는 곳
	@PostMapping(value = "/sendToUser")
	public String sendToUser(HttpSession session, Model model, @RequestParam("mailList") List<Integer> mailList) {
	    if(session.getAttribute("user") == null) {
	        return "redirect:/";
	    }
	    DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
	    if(!memberVO.getRole().equals("ROLE_ADMIN")) {
	        return "redirect:/";
	    }
	    model.addAttribute("name",memberVO.getName());
	    
	    Map<String, Object> map = new HashMap<>();
	    for(int i : mailList) {
	    	map.put(daangnMemberService.selectByIdx(i).getEmail(), i);
	    }
	    model.addAttribute("userMap", map);
	    return "admin/mailSendToUser";
	}
	
	
	
	@PostMapping(value = "/sendToUserOk")
	public String sendToUserOk(HttpSession session, Model model, @RequestParam(value = "userIdx") List<Integer> userList, @RequestParam("title") String title, @RequestParam("subject") String subject) {
		if(session.getAttribute("user") == null) {
	        return "redirect:/";
	    }
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
	    if(!memberVO.getRole().equals("ROLE_ADMIN")) {
	        return "redirect:/";
	    }
		model.addAttribute("name",memberVO.getName());
	    log.info("title : {}",title);
	    log.info("subject : {}", subject);
	    log.info("userList : {}", userList);
	    model.addAttribute("title", title);
	    model.addAttribute("subject", subject);
	    Map<String, List<DaangnMemberVO>> mailResultMap = mailService.adminMailSend(userList, title, subject);
	    model.addAttribute("mailResultMap", mailResultMap);
	    return "admin/mailToUserResult";
	}
	
	//=========================================================================================================================================
	/**
	 * 회원 권한 변경 페이지
	 * @param session
	 * @param model
	 * @param cv
	 * @return
	 */
	@GetMapping(value = "/user-roles")
	public String userRoles(HttpSession session, Model model, @ModelAttribute(value = "cv") CommonVO cv) {
		if(session.getAttribute("user") == null) {
	        return "redirect:/";
	    }
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
	    if(!memberVO.getRole().equals("ROLE_ADMIN")) {
	        return "redirect:/";
	    }
	    model.addAttribute("name", memberVO.getName());
	    cv.setS(20);
	    log.info("userRoles 실행 cv: {}", cv);
	    PagingVO<DaangnMemberVO> pv = daangnMemberService.getUsers(cv);
		model.addAttribute("pv", pv);
		model.addAttribute("cv", cv);
		return "admin/userRoles";
	}
	
	@PostMapping(value = "/updateUserRole")
	public String updateUserRole(HttpSession session, @ModelAttribute(value = "cv") CommonVO cv, @RequestParam(value = "idx") int idx, @RequestParam(value = "role") int role) {
		log.info("updateUserRole 실행 => cv:{}, role:{}", cv, role);
		DaangnMemberVO memberVO = new DaangnMemberVO();
		memberVO.setIdx(idx);
		if(role == 0) {
			memberVO.setRole("ROLE_USER");
		} else {
			memberVO.setRole("ROLE_ADMIN");
		}
		log.info("memberVO: {}", memberVO);
		daangnMemberService.update(memberVO);
		return "redirect:/adm/user-roles?p="+cv.getP()+"&search="+cv.getSearch();
	}
	
	
	// ==========================
	// 회원 동향분석
	@GetMapping(value = "/userTrendAnalysis")
	public String userTrendAnalysis(HttpSession session, Model model, @ModelAttribute(value = "cv") CommonVO cv) {
		log.info("userTrendAnalysis실행 cv: {}",cv);
		if(session.getAttribute("user") == null) {
	        return "redirect:/";
	    }
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
	    if(!memberVO.getRole().equals("ROLE_ADMIN")) {
	        return "redirect:/";
	    }
	    model.addAttribute("name", memberVO.getName());
		cv.setS(30);
		PagingVO<PopularVO> pv = popularService.getUserTrendAnalysis(cv);
		log.info("pv => {}", pv);
		model.addAttribute("pv", pv);
		model.addAttribute("cv", cv);
		return "admin/userTrendAnalysis";
	}
	
	//=========================================================================================================================================
	// **게시물관리**
	//=========================================================================================================================================
	// 인기게시물
	//=========================================================================================================================================
	@GetMapping(value = "/bestPost")
	public String bestPost(HttpSession session, Model model) {
		log.info("bestPost실행");
		if(session.getAttribute("user") == null) {
	        return "redirect:/";
	    }
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
	    if(!memberVO.getRole().equals("ROLE_ADMIN")) {
	        return "redirect:/";
	    }
	    model.addAttribute("name", memberVO.getName());
		
		List<DaangnMainBoardVO> popularBoardList = popularService.findPopularBoard();
		model.addAttribute("list", popularBoardList);
		log.info("popularBoardList : {}", popularBoardList);
		return "admin/bestPost";
	}
	
	//===========================================================
	// 게시판 관리
	@GetMapping(value = "/boardManagement")
	public String boardMangement(HttpSession session, @ModelAttribute(value = "cv") CommonVO cv, Model model) {
		if(session.getAttribute("user") == null) {
	        return "redirect:/";
	    }
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
	    if(!memberVO.getRole().equals("ROLE_ADMIN")) {
	        return "redirect:/";
	    }
	    model.addAttribute("name", memberVO.getName());
		PagingVO<DaangnMainBoardVO> pv = daangnMainBoardService.selectList(cv);
        log.info("boardMangement실행 cv: {}",cv);
        model.addAttribute("pv",pv);
        model.addAttribute("cv", cv);
        return "admin/boardManagement";
	}
	
	//=========================================================================================================================================
	// 방문객 정보를 넘기는 주소
	@PostMapping(value = "/visitorData")
	@ResponseBody
	public Map<String, Object> visitorData(){
		Map<String, Object> map = new HashMap<>();
		// 1. 현재접속자수
		map.put("activeSession", VisitService.getActiveSessionCount());
		// 2. 지금까지 총 방문자 수
		map.put("totalCount", visitService.getTotalVisitorCount());
		
		// 현재로 부터 4일전 3일전 2일전 1일전 오늘의 방문자수를 가지는 리스트
		List<Integer> countList = new ArrayList<>();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date()); // 오늘날짜 새팅
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		cal.add(Calendar.DATE, -6);
		for (int i = 0; i < 7; i++) {
			String strDate = sdf.format(cal.getTime());
			int visitCount = visitService.getDailyVisitorCount(strDate);
			countList.add(visitCount);
			cal.add(Calendar.DATE, +1);
		}
		map.put("countList", countList);
		return map;
	}
	
	
	//=====================================================================================================================
	// 공지사항 관리
	//=====================================================================================================================
	/** 공지사항 쓰기 페이지 */
	@GetMapping(value = "/noticeUpload")
	public String noticeUpload(HttpSession session, Model model) {
		if(session.getAttribute("user") == null) {
	        return "redirect:/";
	    }
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
	    if(!memberVO.getRole().equals("ROLE_ADMIN")) {
	        return "redirect:/";
	    }
	    model.addAttribute("name", memberVO.getName());
		return "admin/noticeUpload";
	}
	/** 썸머노트를 사용할때 사진을 올리는 주소 */
	@PostMapping(value = "/notice/fileUpload")
	@ResponseBody
	public Map<String, Object> fileUpload(HttpServletRequest request, @RequestPart(value = "file", required = false) MultipartFile file) {
		// json 데이터로 등록
		// {"uploaded" : 1, "fileName" : "test.jpg", "url" : "/img/test.jpg"}
		// 이런 형태로 리턴이 나가야함.
		String uploadPath = request.getServletContext().getRealPath("/upload/");
		// 파일 객체 생성
		File file2 = new File(uploadPath);
		if (!file2.exists()) {
			file2.mkdirs();
		}
		log.info("서버 실제 경로 : " + uploadPath);
		String saveName = "";
		String saveFileName = "";
		if (file != null && file.getSize() > 0) { // 파일이 넘어왔다면
			try {
				saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
				log.info("saveFileName => {}", saveFileName);
				// 파일 객체를 만들어 저장해 준다.
				File saveFile = new File(uploadPath, saveFileName);
				// 파일 복사
				FileCopyUtils.copy(file.getBytes(), saveFile);
				saveName = request.getContextPath() + "/upload/" + saveFileName;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uploaded", 1);
		map.put("fileName", saveFileName);
		map.put("url", saveName);
		return map;
	}
	
	/** 공지사항 쓰기 get 방지 주소 */
	@GetMapping(value = "/noticeUploadOk")
	public String noticeUploadOkGet() {
		return "redirect:/";
	}
	
	/** 공지사항 쓰기 업로드 */
	@PostMapping(value = "/noticeUploadOk")
	public String noticeUploadOkPost(HttpSession session, @ModelAttribute(value = "notice") NoticesVO nv) {
		daangnNoticesService.insertNotices(nv);
		log.info("noticeUploadOk 실행 => {}", nv);
		return "redirect:/notice/detail/" + nv.getIdx(); // 업로드페이지로 돌아감!
	}
	
	/** 공지사항 수정하기 페이지 (완) */
	@PostMapping(value = "/noticeUpdate")
	public String noticeUpdate(HttpSession session, Model model, @RequestParam(value = "idx") int idx) {
		if(session.getAttribute("user") == null) {
	        return "redirect:/";
	    }
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
	    if(!memberVO.getRole().equals("ROLE_ADMIN")) {
	        return "redirect:/";
	    }
	    NoticesVO nv = daangnNoticesService.getNoticesByIdx(idx);
	    model.addAttribute("name", memberVO.getNickName());
	    model.addAttribute("notice", nv);
		return "admin/noticeUpdate";
	}
	
	@GetMapping(value = "/noticeUpdateOk")
	public String noticeUpdateOkGet() {
		return "redirect:/";
	}
	
	/** 공지사항 수정하기 */
	@PostMapping(value = "/noticeUpdateOk")
	public String noticeUpdateOkPost(HttpSession session, @ModelAttribute(value = "notice") NoticesVO nv) {
		daangnNoticesService.updateNotices(nv);
		log.info("noticeUploadOk 실행 => {}", nv);
		return "redirect:/notice/detail/" + nv.getIdx(); // 업로드페이지로 돌아감!
	}
	
	@DeleteMapping(value = "/noticeDelete/{idx}")
	@ResponseBody
	public String noticeDeleteOk(@PathVariable(value = "idx") int idx) {
		log.info("noticeDeleteOk 실행 => {}", idx);
		int result = daangnNoticesService.deleteByIdx(idx);
		log.info("noticeDeleteOk 리턴 => {}", result);
		return result + "";
	}
}
