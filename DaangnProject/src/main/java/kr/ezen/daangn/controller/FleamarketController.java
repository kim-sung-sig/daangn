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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.DaangnBoardFileService;
import kr.ezen.daangn.service.DaangnLikeService;
import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnFileVO;
import kr.ezen.daangn.vo.DaangnLikeVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import kr.ezen.daangn.vo.PagingVO;
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
	public String list(Model model
					   , @PathVariable(value = "region", required = false) String region
					   , @PathVariable(value = "gu", required = false) String gu
					   , @PathVariable(value = "dong", required = false) String dong
					   , @RequestParam(value = "search", required = false) String search
					   ) {
		log.debug("list 실행 region: {}, gu: {}, dong: {}, search: {}, p: {}", region, gu, dong, search);

		CommonVO commonVO = new CommonVO(region, gu, dong, search);
		commonVO.setS(18);
		commonVO.setB(5);
		PagingVO<DaangnMainBoardVO> pv = daangnMainBoardService.selectList(commonVO);
		
		model.addAttribute("pv", pv);
		
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
		return "fleamarket/fleamarket";
	}
	
	/**
	 * 중고거래 글 하나 보기
	 * @param model
	 * @param session
	 * @param idx
	 * @return
	 */
	@GetMapping(value = "/fleamarketDetail/{idx}")
	public String fleamarketDetail(Model model, HttpSession session, @PathVariable(value = "idx") int idx){
		DaangnMainBoardVO board = daangnMainBoardService.selectByIdx(idx);
		
		log.info("fleamarketDetail/idx 실행 => idx : {} , board : {}", idx, board);
		
		// 좋아요 했는지 안했는지 체크
		if(session.getAttribute("user") != null) {
			DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
			DaangnLikeVO likeVO = new DaangnLikeVO();
			likeVO.setUserIdx(memberVO.getIdx());
			likeVO.setBoardIdx(idx);
			model.addAttribute("likeCheck", daangnLikeService.select(likeVO));
		}
		model.addAttribute("board", board);
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
	public String flfleamarketUploadOkPost(HttpSession session, MultipartHttpServletRequest request, @ModelAttribute(value = "boardVO") DaangnMainBoardVO boardVO) {
		log.info("flfleamarketUploadOkPost 실행 : {}", boardVO);
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
		return "redirect:/fleamarket";
	}
}
