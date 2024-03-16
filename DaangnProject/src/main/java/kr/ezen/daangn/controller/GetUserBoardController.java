package kr.ezen.daangn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.ScrollVO;
import lombok.extern.slf4j.Slf4j;

/** 유저의 board를 얻을 수 있는 주소 + 마이페이지가 아닌 유저의 글목록보는 페이지*/
@Slf4j
@Controller
@RequestMapping("/user")
public class GetUserBoardController {
	
	@Autowired
	private DaangnMainBoardService daangnMainBoardService;
	
	/** 유저번호의 게시글목록을 얻을수 있는 페이지 */
	@PostMapping(value = "/{userIdx}/boards")
	@ResponseBody
	public List<DaangnMainBoardVO> getBoardsByUserIdx(@PathVariable(value = "userIdx") int userIdx, @RequestBody ScrollVO sv){
		List<DaangnMainBoardVO> list = null;
		log.info("getBoardsByUserIdx 실행 idx => {}, sv => {}", userIdx, sv);
		sv.setUserRef(userIdx);
		list = daangnMainBoardService.selectScrollList(sv);
		log.info("getBoardsByUserIdx 리턴 {}개 => {}", list.size(), list);
		return list;
	}
}
