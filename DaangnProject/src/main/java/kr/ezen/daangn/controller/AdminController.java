package kr.ezen.daangn.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.service.MailService;
import kr.ezen.daangn.service.PopularService;
import kr.ezen.daangn.service.VisitService;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
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
	@ResponseBody()
	public List<DaangnMemberVO> pagingUser(@RequestBody Map<String, String> map){
		CommonVO cv = new CommonVO();
		cv.setP(Integer.parseInt(map.get("currentPage")));
		cv.setSearch(map.get("search"));
		cv.setS(20);
		cv.setB(5);
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
	/** 공지사항 목록보기 페이지 home이나 어디로 이관예정*/
	/*
	@GetMapping(value = "/notices")
	public String notice(HttpSession session, Model model, @ModelAttribute CommonVO cv, @RequestParam(value = "error", required = false) String error) {
		if(session.getAttribute("user") == null) {
	        return "redirect:/";
	    }
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
	    if(!memberVO.getRole().equals("ROLE_ADMIN")) {
	        return "redirect:/";
	    }
	    model.addAttribute("name", memberVO.getName());
	    if(error != null) {
	    	model.addAttribute("error", error);
	    }
	    cv.setS(10);
	    cv.setB(5);
	    cv.setCategoryNum(3);
	    PagingVO<JungBoardVO> noticeList = jungBoardService.selectList(cv);
	    model.addAttribute("pv", noticeList);
	    return "admin/notices";
	}
	*/
	
	/** 공지사항 쓰기 페이지 */
	@GetMapping(value = "/noticeUpload")
	public String noticeUpload(HttpSession session, Model model, @RequestParam(value = "error", required = false) String error) {
		if(session.getAttribute("user") == null) {
	        return "redirect:/";
	    }
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
	    if(!memberVO.getRole().equals("ROLE_ADMIN")) {
	        return "redirect:/";
	    }
	    model.addAttribute("name", memberVO.getName());
	    if(error != null) {
	    	model.addAttribute("error", error);
	    }
		return "admin/noticeUpload";
	}
	
	/** 공지사항 쓰기 get 방지 주소 */
	@GetMapping(value = "/noticeUploadOk")
	public String noticeUploadOkGet() {
		return "redirect:/";
	}
	/** 공지사항 쓰기 업로드 */
	/*
	@PostMapping(value = "/noticeUploadOk")
	public String noticeUploadOkPost(HttpSession session, @ModelAttribute(value = "board") JungBoardVO boardVO, MultipartHttpServletRequest request) {
		if(session.getAttribute("user") == null) {
			return "redirect:/";
		}
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
		if(!memberVO.getRole().equals("ROLE_ADMIN")) {
			return "redirect:/";
		}
		boardVO.setRef(memberVO.getIdx());
		jungBoardService.insert(boardVO);
		
		String uploadPath = request.getServletContext().getRealPath("./upload/");
		
		 File file2 = new File(uploadPath);
	     if (!file2.exists()) {
	        file2.mkdirs();
	     }
	    log.info("서버 실제 경로 : " + uploadPath); // 확인용
	    
        List<MultipartFile> list = request.getFiles("file"); // form에 있는 name과 일치
        String url = "";
        String filepath = "";
        try {
           if (list != null && list.size() > 0) { // 받은 파일이 존재한다면
              // 반복해서 받는다.
              for (MultipartFile file : list) {
                 // 파일이 없으면 처리하지 않는다.
                 if (file != null && file.getSize() > 0) {
                    // 저장파일의 이름 중복을 피하기 위해 저장파일이름을 유일하게 만들어 준다.
                    String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    // 파일 객체를 만들어 저장해 준다.
                    File saveFile = new File(uploadPath, saveFileName);
                    // 파일 복사
                    FileCopyUtils.copy(file.getBytes(), saveFile);
                    
                    url = file.getOriginalFilename();	// original
                    filepath = saveFileName;			// savefileName
                    JungFileBoardVO fileBoardVO = new JungFileBoardVO();
                    fileBoardVO.setUrl(url);
                    fileBoardVO.setFilepath(filepath);
                    fileBoardVO.setRef(boardVO.getIdx());
                    jungFileBoardService.insert(fileBoardVO);
                 }
              }
           }
           return "redirect:/adm/notice/" + boardVO.getIdx(); // 글 하나보기로 이동!
        } catch (Exception e) {
           e.printStackTrace();
        }
		return "redirect:/adm/noticeUpload?error=failUpload"; // 업로드페이지로 돌아감!
	}
	*/
	
	/** 공지 사항 1개 보기 이것도 이관예정 + 고정된거 하나얻기 만들꺼*/
	/*
	@GetMapping(value = "/notice/{idx}")
	public String notice(HttpSession session, Model model, @PathVariable(value = "idx") int idx) {
		if(session.getAttribute("user") == null) {
	        return "redirect:/";
	    }
		
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
	    if(!memberVO.getRole().equals("ROLE_ADMIN")) {
	        return "redirect:/";
	    }
	    
	    model.addAttribute("name", memberVO.getNickName());
	    
	    JungBoardVO boardVO = jungBoardService.selectByIdx(idx);
	    if (boardVO == null) {
	        log.info("notice null or category num not matched");
	        return "redirect:/adm/notices?error=notFound"; // 목록으로 돌아감!
	    }
	    model.addAttribute("board", boardVO);
		return "admin/notice";
	}
	*/
	/** 공지사항 수정하기 페이지 */
	/*
	@GetMapping(value = "/noticeUpdate/{idx}")
	public String noticeUpdate(@PathVariable(value = "idx") int idx, HttpSession session, Model model, @RequestParam(value = "error", required = false) String error) {
		if(session.getAttribute("user") == null) {
	        return "redirect:/";
	    }
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
	    if(!memberVO.getRole().equals("ROLE_ADMIN")) {
	        return "redirect:/";
	    }
	    JungBoardVO boardVO = jungBoardService.selectByIdx(idx);
	    if(boardVO == null ||boardVO.getRef() != memberVO.getIdx()) {
	    	return "redirect:/";
	    }
	    if(error != null) {
	    	model.addAttribute("error", error);
	    }
	    model.addAttribute("name", memberVO.getNickName());
	    model.addAttribute("board", boardVO);
		return "admin/noticeUpload";
	}
	*/
	
	/** 공지사항 수정하기 */
	/*
	@PostMapping(value = "/noticeUpdateOk")
	@Transactional
	public String noticeUpdateOk(HttpSession session, MultipartHttpServletRequest request, @ModelAttribute(value = "board") JungBoardVO boardVO) {
		if(session.getAttribute("user") == null) {
			return "redirect:/";
		}
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
		if(!memberVO.getRole().equals("ROLE_ADMIN")) {
			return "redirect:/";
		}
		jungFileBoardService.deleteByRef(boardVO.getIdx());			// 파일 지우고
		jungBoardService.update(boardVO);							// 게시글 업데이트하고
		String uploadPath = request.getServletContext().getRealPath("./upload/");
		
		 File file2 = new File(uploadPath);
	     if (!file2.exists()) {
	        file2.mkdirs();
	     }
	    log.info("서버 실제 경로 : " + uploadPath); // 확인용
	    
        List<MultipartFile> list = request.getFiles("file"); // form에 있는 name과 일치
        String url = "";
        String filepath = "";
        try {
           if (list != null && list.size() > 0) { // 받은 파일이 존재한다면
              // 반복해서 받는다.
              for (MultipartFile file : list) {
                 // 파일이 없으면 처리하지 않는다.
                 if (file != null && file.getSize() > 0) {
                    // 저장파일의 이름 중복을 피하기 위해 저장파일이름을 유일하게 만들어 준다.
                    String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    // 파일 객체를 만들어 저장해 준다.
                    File saveFile = new File(uploadPath, saveFileName);
                    // 파일 복사
                    FileCopyUtils.copy(file.getBytes(), saveFile);
                    
                    url = file.getOriginalFilename();	// original
                    filepath = saveFileName;			// savefileName
                    JungFileBoardVO fileBoardVO = new JungFileBoardVO();
                    fileBoardVO.setUrl(url);
                    fileBoardVO.setFilepath(filepath);
                    fileBoardVO.setRef(boardVO.getIdx());
                    jungFileBoardService.insert(fileBoardVO);
                 }
              }
           }
           return "redirect:/adm/notice/" + boardVO.getIdx(); // 글 하나보기로 이동!
        } catch (Exception e) {
           e.printStackTrace();
        }
		return "redirect:/adm/noticeUpdate/"+boardVO.getIdx()+"?error=failUpdate"; // 업로드페이지로 돌아감!
	}
	*/
}
