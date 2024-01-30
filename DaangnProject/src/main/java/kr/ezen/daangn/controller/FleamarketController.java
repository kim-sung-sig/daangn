package kr.ezen.daangn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.suport.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Configuration
@RequestMapping(value = "/fleamarket")
@Slf4j
public class FleamarketController {
	@Autowired
	private DaangnMainBoardService mainBoardService;
	@Autowired
	private DaangnMemberService memberService;
	
	@GetMapping(value = {"","/", "/{region}", "/{region}/{gu}", "/{region}/{gu}/{dong}"})
	public String list(Model model
					   , @PathVariable(value = "region", required = false) String region
					   , @PathVariable(value = "gu", required = false) String gu
					   , @PathVariable(value = "dong", required = false) String dong) {
		log.debug("list 실행");
		
		
		CommonVO commonVO = new CommonVO(region,gu,dong,""); // 검색어는 우선 비워두겠다.
		List<DaangnMainBoardVO> list = mainBoardService.selectList(commonVO);
		for(DaangnMainBoardVO boardVO : list) {
			DaangnMemberVO memberVO = memberService.selectByIdx(boardVO.getRef());
			boardVO.setMemberVO(memberVO);
		}
		if(region != null){
			model.addAttribute("region", region);
		}
		if(gu != null){
			model.addAttribute("gu", gu);
		}
		if(dong != null){
			model.addAttribute("dong", dong);
		}
		model.addAttribute("list", list);
		return "list";
	}
}
