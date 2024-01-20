package kr.ezen.daangn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.ezen.daangn.service.BoardService;
import kr.ezen.daangn.vo.BoardVO;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.PagingVO;

@Controller
public class HomeController {
	
	@Autowired
	BoardService boardService;

	@GetMapping(value = { "/", "/main", "/index" })
	public String home() {
		return "index";
	}
	
	@GetMapping(value = "/list")
	public String listGet(Model model) {
		CommonVO commonVO = new CommonVO();
		PagingVO<BoardVO> pv = boardService.selectList(commonVO);
		model.addAttribute("pv", pv);
		return "list";
	}
	@PostMapping(value = "/list")
	public String listPost(Model model, @ModelAttribute(value = "commonVO") CommonVO commonVO) {
		PagingVO<BoardVO> pv = boardService.selectList(commonVO);
		model.addAttribute("pv", pv);
		return "list";
	}

}
