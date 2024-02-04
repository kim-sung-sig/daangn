package kr.ezen.daangn.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.DaangnLikeService;
import kr.ezen.daangn.vo.DaangnLikeVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(value = "/api")
public class ApiController {
	@Autowired
	private DaangnLikeService daangnLikeService;
	
	@PostMapping(value = "/like")
	@ResponseBody
	public String like(HttpSession session, @RequestBody Map<String, Integer> map) {
		if(session.getAttribute("user") == null) {
			return "0";
		}
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
		log.info("like 실행 : userIdx :{}, boardIdx :{}", memberVO.getIdx(), map.get("boardIdx"));
		DaangnLikeVO likeVO = new DaangnLikeVO();
		likeVO.setUserIdx(memberVO.getIdx());
		likeVO.setBoardIdx(map.get("boardIdx"));
		int result = daangnLikeService.insertLike(likeVO);
		log.info("unlike result = {}", result);
		return result+"";
	}
	
	@PostMapping(value = "/unlike")
	@ResponseBody
	public String unlike(HttpSession session, @RequestBody Map<String, Integer> map) {
		if(session.getAttribute("user") == null) {
			return "0";
		}
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
		log.info("unlike 실행 : userIdx :{}, boardIdx :{}", memberVO.getIdx(), map.get("boardIdx"));
		DaangnLikeVO likeVO = new DaangnLikeVO();
		likeVO.setUserIdx(memberVO.getIdx());
		likeVO.setBoardIdx(map.get("boardIdx"));
		int result = daangnLikeService.deleteLike(likeVO);
		log.info("unlike result = {}", result);
		return result+"";
	}
	
	
	
}
