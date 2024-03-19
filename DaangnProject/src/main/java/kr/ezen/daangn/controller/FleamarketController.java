package kr.ezen.daangn.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.ChatService;
import kr.ezen.daangn.service.DaangnBoardFileService;
import kr.ezen.daangn.service.DaangnLikeService;
import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.service.PopularService;
import kr.ezen.daangn.service.ReserveService;
import kr.ezen.daangn.vo.DaangnFileVO;
import kr.ezen.daangn.vo.DaangnLikeVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import kr.ezen.daangn.vo.PopularVO;
import kr.ezen.daangn.vo.ReserveVO;
import kr.ezen.daangn.vo.ScrollVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Configuration
@Slf4j
public class FleamarketController {
	
	@Autowired
	private DaangnMainBoardService daangnMainBoardService;
	@Autowired
	private DaangnLikeService daangnLikeService;
	@Autowired
	private DaangnBoardFileService daangnBoardFileService;
	@Autowired
	private PopularService popularService;
	@Autowired
	private ChatService chatService;
	@Autowired
	private DaangnMemberService daangnMemberService;
	@Autowired
	private ReserveService reserveSerivce;
	
	/**
	 * 중고거래 리스트 보기
	 * @param model
	 * @param region
	 * @param gu
	 * @param dong
	 * @param search
	 * @return
	 */
	@GetMapping(value = {"/fleamarket","/fleamarket/", "/fleamarket/{region}", "/fleamarket/{region}/{gu}", "/fleamarket/{region}/{gu}/{dong}"})
	public String list(Model model, HttpSession session, @PathVariable(value = "region", required = false) String region, @PathVariable(value = "gu", required = false) String gu, @PathVariable(value = "dong", required = false) String dong, @RequestParam(value = "search", required = false) String search, @RequestParam(value = "isOk", required = false) String isOk) {
		log.debug("list 실행 region: {}, gu: {}, dong: {}, search: {}", region, gu, dong, search);
		if(isOk != null) {
			model.addAttribute("isOk", isOk);
		}
		if(session.getAttribute("user") != null) {
			DaangnMemberVO user = (DaangnMemberVO) session.getAttribute("user");
			DaangnMemberVO dbUserData = daangnMemberService.selectByIdx(user.getIdx());
			if(dbUserData != null) {
				model.addAttribute("chatUnreadCount",chatService.selectUnReadCountByUserIdx(dbUserData.getIdx()));				
			}
		}
		// 추가로 지역정보리스트 리턴!
		model.addAttribute("regionList", daangnMainBoardService.regionList(null,null,null));
		if (region != null) {
			model.addAttribute("region", region);
			model.addAttribute("guList", daangnMainBoardService.regionList(region, null, null));			
		}
		if (gu != null) {
			model.addAttribute("gu", gu);
			model.addAttribute("dongList", daangnMainBoardService.regionList(region, gu, null));			
		}
		if(search != null && search.trim().length() > 0) {
			model.addAttribute("search", search);
		}
		model.addAttribute("lastItemIdx", daangnMainBoardService.getLastIdx());
		return "fleamarket/fleamarket";
	}
	
	@PostMapping(value = "/getfleamarketList")
	@ResponseBody
	public List<DaangnMainBoardVO> getFleamarketList(@RequestBody ScrollVO sv){
		log.info("getfleamarketList 실행 : {}", sv);
		List<DaangnMainBoardVO> list = daangnMainBoardService.selectScrollList(sv);
		log.info("getfleamarketList 리턴 : {}개", list.size());
		return list;
	}
	
	/**
	 * 중고거래 글 하나 보기
	 * @param model
	 * @param session
	 * @param idx
	 * @return
	 */
	@GetMapping(value = "/fleamarketDetail/{idx}")
	public String fleamarketDetail(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "idx") int idx){
		log.info("fleamarketDetail/idx 실행 => idx : {}", idx);
		// 조회수 증가로직 세션방식
		/*
		String key = "VISITED_"+ idx;
		Boolean isVisited = (Boolean) session.getAttribute(key);
		if(isVisited == null || !isVisited) {
			daangnMainBoardService.updateReadCount(idx);
			session.setAttribute(key, true);	
		}
		*/
		if(session.getAttribute("user") != null) {
			DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
			DaangnLikeVO likeVO = new DaangnLikeVO();
			likeVO.setUserIdx(memberVO.getIdx());
			likeVO.setBoardIdx(idx);
			model.addAttribute("likeCheck", daangnLikeService.select(likeVO)); // 좋아요 했는지 안했는지 체크
		}
		// 조회수 증가로직 쿠키 방식
		Cookie oldCookie = null;
		Cookie[] cookies = request.getCookies();
		final String COOKIE_NAME = "fleamarket";
		final int COOKIE_MAX_AGE = 60;

		if (cookies != null) {
		    for (Cookie cookie : cookies) {
		        if (cookie.getName().equals(COOKIE_NAME)) {
		            oldCookie = cookie;
		            break;
		        }
		    }
		}

		if (oldCookie != null) {
		    String cookieValue = oldCookie.getValue();
		    if (!cookieValue.contains("[" + idx + "]")) {
		        daangnMainBoardService.updateReadCount(idx);
		        oldCookie.setValue(cookieValue + "_[" + idx + "]");
		        oldCookie.setMaxAge(COOKIE_MAX_AGE);
		        response.addCookie(oldCookie);
		        if(session.getAttribute("user") != null) {
		        	DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
		        	PopularVO p = new PopularVO();
		        	p.setBoardRef(idx);
		        	p.setUserRef(memberVO.getIdx());
		        	p.setInteraction(1);
		        	popularService.insertPopular(p);
		        }
		    }
		} else {
		    daangnMainBoardService.updateReadCount(idx);
		    Cookie newCookie = new Cookie(COOKIE_NAME, "[" + idx + "]");
		    newCookie.setMaxAge(COOKIE_MAX_AGE);
		    response.addCookie(newCookie);
		    if(session.getAttribute("user") != null) {
	        	DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
	        	PopularVO p = new PopularVO();
	        	p.setBoardRef(idx);
	        	p.setUserRef(memberVO.getIdx());
	        	p.setInteraction(1);
	        	popularService.insertPopular(p);
	        }
		}
		DaangnMainBoardVO board = daangnMainBoardService.selectByIdx(idx);
		if(board == null) {
			return "redirect:/fleamarket";
		}
		model.addAttribute("board", board);
		// 이 글의 유저가 쓴 다른 글들!;
		model.addAttribute("userBoard", daangnMainBoardService.selectListByUserIdxAndNotBoardIdx(board.getUserRef(), board.getIdx()));
		return "fleamarket/fleamarketDetail";
	}
	
	/**
	 * 중고거래 글 쓰기
	 * @param session
	 * @return
	 */
	@GetMapping(value = "/fleamarketUpload")
	public String fleamarketUpload(HttpSession session) {
		if(session.getAttribute("user") == null) {
			return "redirect:/";
		}
		return "fleamarket/fleamarketUpload";
	}
	
	/**
	 * 중고거래 글 쓰기 get방지
	 * @return
	 */
	@GetMapping("/fleamarketUploadOk")
	public String fleamarketUploadOkGet() {
		return "redirect:/";
	}
	
	/**
	 * 중고거래 글 저장하기
	 * @param session
	 * @param request
	 * @param boardVO
	 * @return
	 */
	@PostMapping("/fleamarketUploadOk")
	@Transactional
	public String fleamarketUploadOkPost(HttpSession session, MultipartHttpServletRequest request, @ModelAttribute(value = "boardVO") DaangnMainBoardVO boardVO) {
		log.info("fleamarketUploadOkPost 실행 : {}", boardVO);
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
		boardVO.setUserRef(memberVO.getIdx());
		daangnMainBoardService.saveMainBoard(boardVO); // 저장된 idx_seq를 리턴한다!
		int boardIdx = boardVO.getIdx();
		log.info("boardIdx = {}", boardIdx);
		// file save
		String uploadPath = request.getServletContext().getRealPath("/upload/");
		// 파일 객체 생성
		File file2 = new File(uploadPath);
		// 폴더가 없다면 폴더를 생성해준다.
		if (!file2.exists()) {
			file2.mkdirs();
		}
		
		log.info("서버 실제 경로 : " + uploadPath);
		// --------------------------------------------------------------------------------------
		List<MultipartFile> list = request.getFiles("file"); // form에 있는 name과 일치
		try {
			if (list != null && list.size() > 0) {
				for(MultipartFile file :list) {
					if(file != null && file.getSize() >0) {
						String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
						String originFileName = file.getOriginalFilename();
						File savaFile = new File(uploadPath, saveFileName);
						FileCopyUtils.copy(file.getBytes(), savaFile);
						
						DaangnFileVO fileVO = new DaangnFileVO();
						fileVO.setRef(boardIdx);
						fileVO.setSaveFileName(saveFileName);
						fileVO.setOriginFileName(originFileName);
						log.info("fileVO : {}", fileVO);
						daangnBoardFileService.insert(fileVO);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/fleamarketDetail/"+boardIdx;
	}
	
	
	/**
	 * 글 수정할 수 있는 페이지
	 */
	@GetMapping(value = "/fleamarketUpdate/{idx}")
	public String fleamarketUpdate(@PathVariable(value = "idx") int idx, Model model, HttpSession session) {
		if(session.getAttribute("user") == null) {
			return "redirect:/";
		}
		DaangnMainBoardVO boardVO = daangnMainBoardService.selectByIdx(idx);
		DaangnMemberVO user = (DaangnMemberVO) session.getAttribute("user");
		if(boardVO.getUserRef() != user.getIdx()) {
			return "redirect:/";
		}
		model.addAttribute("board", boardVO);
		return "fleamarket/fleamarketUpdate";
	}
	
	/**
	 * 중고거래 글 수정하기
	 * @param session
	 * @param request
	 * @param boardVO
	 * @return 
	 */
	@PostMapping("/fleamarketUpdateOk")
	@Transactional
	public String fleamarketUpdateOkPost(HttpSession session, MultipartHttpServletRequest request, @ModelAttribute(value = "boardVO") DaangnMainBoardVO boardVO) {
		log.info("fleamarketUpdateOkPost 실행 : {}", boardVO);
		int userRef = daangnMainBoardService.selectByIdx(boardVO.getIdx()).getUserRef();
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
		if(userRef != memberVO.getIdx()) {
			return "redirect:/";
		}
		boardVO.setUserRef(memberVO.getIdx());
		daangnMainBoardService.update(boardVO);					// 업데이트
		daangnBoardFileService.deleteByBoardIdx(boardVO.getIdx());	// 기존 사진 지우고 새로 업로드
		// file save
		String uploadPath = request.getServletContext().getRealPath("/upload/");
		// 파일 객체 생성
		File file2 = new File(uploadPath);
		// 폴더가 없다면 폴더를 생성해준다.
		if (!file2.exists()) {
			file2.mkdirs();
		}
		log.info("서버 실제 경로 : " + uploadPath);
		// --------------------------------------------------------------------------------------
		List<MultipartFile> list = request.getFiles("file"); // form에 있는 name과 일치
		try {
			if (list != null && list.size() > 0) {
				for(MultipartFile file :list) {
					if(file != null && file.getSize() >0) {
						String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
						String originFileName = file.getOriginalFilename();
						File savaFile = new File(uploadPath, saveFileName);
						FileCopyUtils.copy(file.getBytes(), savaFile);
						
						DaangnFileVO fileVO = new DaangnFileVO();
						fileVO.setRef(boardVO.getIdx());
						fileVO.setSaveFileName(saveFileName);
						fileVO.setOriginFileName(originFileName);
						log.info("fileVO : {}", fileVO);
						daangnBoardFileService.insert(fileVO);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/fleamarketDetail/" + boardVO.getIdx();
	}
	
	@PostMapping(value = "/fleamarketDeleteOk")
	@Transactional
	public String fleamarketDeleteOk(HttpSession session, @ModelAttribute(value = "boardVO") DaangnMainBoardVO boardVO) {
		log.info("fleamarketDeleteOk 실행 : {}", boardVO);
		int userRef = daangnMainBoardService.selectByIdx(boardVO.getIdx()).getUserRef();
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
		if(userRef != memberVO.getIdx()) {
			return "redirect:/";
		}
		daangnBoardFileService.deleteByBoardIdx(boardVO.getIdx());
		daangnMainBoardService.deleteByIdx(boardVO.getIdx());
		return "redirect:/fleamarket?isOk=ok";
	}
	
	@PostMapping(value = "/fleamarketStatusUpdate")
	public String fleamarketStatusUpdate(@RequestParam(value = "boardIdx") int boardIdx, @RequestParam(value = "statusRef") int statusRef, Model model, HttpSession session) {
		if(session.getAttribute("user") == null) {
			return "redirect:/";
		}
		DaangnMainBoardVO boardVO = daangnMainBoardService.selectByIdx(boardIdx);
		DaangnMemberVO user = (DaangnMemberVO) session.getAttribute("user");
		if(boardVO.getUserRef() != user.getIdx()) {
			return "redirect:/";
		}
		log.info("fleamarketStatusUpdate 실행 boardIdx => {}, statusRef => {}", boardIdx, statusRef);
		// 1. 예약목록이 있는지 확인한다!
		ReserveVO rv = reserveSerivce.getTBReserveByBoardRef(boardIdx);
		if(rv == null) {
			// 예약목록이 없으면 1번 금지!
			if(statusRef == 1) {
				return "redirect:/";
			}
		} else {
			// interaction=1 이면 1번으로 금지! 리다이렉트
			if(rv.getInteraction() ==1 && statusRef == 1) {
				return "redirect:/";
			}
		}
		model.addAttribute("rv", rv);
		// 예약목록이 있고 interaction = 0 이면 2빼고 1,2,3 셋 다가능
		model.addAttribute("board", boardVO);
		// 1. 게시글에 해당하는 채팅유저를 가져온다.
		List<DaangnMemberVO> chatUsers = chatService.selectChatRoomByBoardIdx(boardIdx);
		log.info("fleamarketStatusUpdate 리턴 chatUsers => {}개 , {}", chatUsers.size(), chatUsers);
		model.addAttribute("chatUsers", chatUsers);
		model.addAttribute("statusRef", statusRef);
		// 2. 예약 목록이 잇는지 확인해서 예약인 번호를 넘겨준다.!
		return "fleamarket/fleamarketStatusUpdate";
	}
	
	@PostMapping(value = "/fleamarketStatusUpdateOk")
	@Transactional
	public String fleamarketStatusUpdateOk(@RequestParam(value = "boardRef") int boardRef, 
										   @RequestParam(value = "statusRef") int statusRef,
										   @RequestParam(value = "userIdx", required = false) Integer userIdx, // 여기부터 commentVOfh 받자
										   @RequestParam(value = "score", required = false) Integer score,
										   @RequestParam(value = "content", required = false) String content,
										   HttpSession session) {
		if(session.getAttribute("user") == null) {
			return "redirect:/";
		}
		DaangnMainBoardVO boardVO = daangnMainBoardService.selectByIdx(boardRef);
		DaangnMemberVO user = (DaangnMemberVO) session.getAttribute("user");
		
		if(boardVO.getUserRef() != user.getIdx()) {
			return "redirect:/";
		}
		log.info("fleamarketStatusUpdateOk 실행 boardRef => {}, statusRef => {}, comment => {}",boardRef, statusRef, content);
		if(statusRef == 1) {			// 예약 취소인 경우!
			reserveSerivce.deleteReserveByBoardIdx(boardRef); // 예약테이블 삭제
			boardVO.setStatusRef(1); // 판매중으로 변환
			daangnMainBoardService.updateStatus(boardVO);
		} else if(statusRef == 2) {		// 예약인 경우!
			ReserveVO rv = new ReserveVO();
			rv.setBoardRef(boardRef);
			rv.setUserRef(userIdx);
			rv.setInteraction(0);
			log.info("rv => {}", rv);
			reserveSerivce.insertReserve(rv); // 예약으로 테이블 저장
			boardVO.setStatusRef(2); // 예약중으로 변환
			daangnMainBoardService.updateStatus(boardVO);	
		} else {						// 판매완료인경우
			// 댓글 입력해주고
			ReserveVO rv = new ReserveVO();
			rv.setBoardRef(boardRef);
			rv.setUserRef(userIdx);
			rv.setInteraction(1);
			reserveSerivce.deleteReserveByBoardIdx(boardRef); // 예약테이블 삭제
			reserveSerivce.insertReserve(rv); // 판매완료로 테이블 저장
			boardVO.setStatusRef(3); // 판매완료로 변환
			daangnMainBoardService.updateStatus(boardVO);
			// 코맨트 서비스를 만들어야되네?ㅠㅠ
		}
		log.info("fleamarketStatusUpdateOk 성공");
		return "redirect:/";
	}
}
