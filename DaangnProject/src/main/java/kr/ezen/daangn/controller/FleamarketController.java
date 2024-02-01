package kr.ezen.daangn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
					   , @PathVariable(value = "dong", required = false) String dong
					   , @RequestParam(value = "search", required = false) String search) {
		log.debug("list 실행 region: {}, gu: {}, dong: {}, search: {}", region, gu, dong, search);
		
		
		CommonVO commonVO = new CommonVO(region, gu, dong, search); // 검색어는 우선 비워두겠다.
		List<DaangnMainBoardVO> list = mainBoardService.selectList(commonVO);
		list.forEach((boardVO)->{
			boardVO.setMemberVO(memberService.selectByIdx(boardVO.getIdx()));
		});
		model.addAttribute("regionList", mainBoardService.regionList(null,null,null));
		if (region != null) {
			model.addAttribute("region", region);
			model.addAttribute("guList", mainBoardService.regionList(region, null, null));			
		}
		if (gu != null) {
			model.addAttribute("gu", gu);
			model.addAttribute("dongList", mainBoardService.regionList(region, gu, null));			
		}
		model.addAttribute("list", list);
		return "list";
	}
}
