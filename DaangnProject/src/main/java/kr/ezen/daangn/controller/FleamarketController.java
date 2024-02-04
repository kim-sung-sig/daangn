package kr.ezen.daangn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.DaangnLikeService;
import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.suport.CommonVO;
import kr.ezen.daangn.vo.DaangnLikeVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Configuration
@Slf4j
public class FleamarketController {
	
	@Autowired
	private DaangnMainBoardService daangnMainBoardService;
	@Autowired
	private DaangnMemberService daangnMemberService;
	@Autowired
	private DaangnLikeService daangnLikeService;
	
	// 리스트보기
	@GetMapping(value = {"/fleamarket","/fleamarket/", "/fleamarket/{region}", "/fleamarket/{region}/{gu}", "/fleamarket/{region}/{gu}/{dong}"})
	public String list(Model model
					   , @PathVariable(value = "region", required = false) String region
					   , @PathVariable(value = "gu", required = false) String gu
					   , @PathVariable(value = "dong", required = false) String dong
					   , @RequestParam(value = "search", required = false) String search) {
		log.debug("list 실행 region: {}, gu: {}, dong: {}, search: {}", region, gu, dong, search);

		CommonVO commonVO = new CommonVO(region, gu, dong, search);
		List<DaangnMainBoardVO> list = daangnMainBoardService.selectList(commonVO);
		list.forEach((boardVO)->{
			boardVO.setMember(daangnMemberService.selectByIdx(boardVO.getIdx()));
			boardVO.setCountLike(daangnLikeService.countLike(boardVO.getIdx()));
		});
		
		model.addAttribute("regionList", daangnMainBoardService.regionList(null,null,null));
		if (region != null) {
			model.addAttribute("region", region);
			model.addAttribute("guList", daangnMainBoardService.regionList(region, null, null));			
		}
		if (gu != null) {
			model.addAttribute("gu", gu);
			model.addAttribute("dongList", daangnMainBoardService.regionList(region, gu, null));			
		}
		
		model.addAttribute("list", list);
		
		return "fleamarket";
	}
	
	
	// 한개 보기
	@GetMapping(value = "/fleamarketDetail/{idx}")
	private String fleamarketDetail(Model model, HttpSession session, @PathVariable(value = "idx") int idx){
		DaangnMainBoardVO board = daangnMainBoardService.selectByIdx(idx);
		log.info("board: {}", board);
		// 유저 정보
		board.setMember(daangnMemberService.selectByIdx(board.getRef()));
		// 파일
		
		// 좋아요수
		log.info("좋아요 수 : {}", daangnLikeService.countLike(board.getIdx()));
		board.setCountLike(daangnLikeService.countLike(board.getIdx()));
		// 체팅수
		
		
		// 좋아요 했는지 안했는지 체크
		if(session.getAttribute("user") != null) {
			DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
			DaangnLikeVO likeVO = new DaangnLikeVO();
			likeVO.setUserIdx(memberVO.getIdx());
			likeVO.setBoardIdx(idx);
			model.addAttribute("likeCheck", daangnLikeService.select(likeVO));
		}
		
		model.addAttribute("board", board);
		return "fleamarketDetail";
	}
}
