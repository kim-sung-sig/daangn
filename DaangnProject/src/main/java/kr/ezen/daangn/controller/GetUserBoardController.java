package kr.ezen.daangn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import kr.ezen.daangn.vo.ScrollVO;
import lombok.extern.slf4j.Slf4j;

/** 유저의 board를 얻을 수 있는 주소 + 마이페이지가 아닌 유저의 글목록보는 페이지*/
@Slf4j
@Controller
@RequestMapping("/user")
public class GetUserBoardController {
	
	@Autowired
	private DaangnMainBoardService daangnMainBoardService;
	@Autowired
	private DaangnMemberService daangnMemberService;
	
	/** 유저번호의 게시글목록을 얻을수 있는 페이지 */
	@PostMapping(value = "/{userIdx}/boards")
	@ResponseBody
	public List<DaangnMainBoardVO> postBoardsByUserIdx(@PathVariable(value = "userIdx") int userIdx, @RequestBody ScrollVO sv){
		List<DaangnMainBoardVO> list = null;
		log.info("postBoardsByUserIdx 실행 idx => {}, sv => {}", userIdx, sv);
		sv.setUserRef(userIdx);
		list = daangnMainBoardService.selectScrollList(sv);
		log.info("postBoardsByUserIdx 리턴 {}개 => {}", list.size(), list);
		return list;
	}
	
	/** 유저글목록 보는 곳 */
	@GetMapping(value = "/{userIdx}/board")
	public String getBoardsByUserIdx(@PathVariable(value = "userIdx") int userIdx, Model model) {
		DaangnMemberVO user = daangnMemberService.selectByIdx(userIdx);
		model.addAttribute("user", user);
		model.addAttribute("lastItemIdx", daangnMainBoardService.getLastIdx());
		model.addAttribute("boardStatus1", daangnMainBoardService.getBoardCountByUserIdxAndStatusRef(user.getIdx(), 1));
    	model.addAttribute("boardStatus2", daangnMainBoardService.getBoardCountByUserIdxAndStatusRef(user.getIdx(), 2));
    	model.addAttribute("boardStatus3", daangnMainBoardService.getBoardCountByUserIdxAndStatusRef(user.getIdx(), 3));
		return "fleamarket/fleamarketUserView";
	}
}
