package kr.ezen.daangn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.PagingVO;
import lombok.extern.slf4j.Slf4j;

/**
 *  생활꿀팁의 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/smartLivingTips")
public class SmartLivingTipsController {
	
	@Autowired
	private DaangnMainBoardService daangnMainBoardService;
	
	@RequestMapping(value = {"","/"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String index(Model model, @ModelAttribute(value = "cv") CommonVO cv) {
		log.info("cv: {}", cv);
		cv.setCategoryNum(13); // 생활 꿀팁은 13번이다
		// PagingVO<DaangnMainBoardVO> pv = daangnMainBoardService.selectList(cv);
		
		model.addAttribute("cv",cv);
		// model.addAttribute("pv",pv);
		return "smartLivingTips/smartLivingTips";
	}
}
