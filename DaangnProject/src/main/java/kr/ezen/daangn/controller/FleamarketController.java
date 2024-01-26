package kr.ezen.daangn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.service.DaangnMemberService;

@Controller
@Configuration
@RequestMapping(value = "/fleamarket")
public class FleamarketController {
	@Autowired
	private DaangnMainBoardService mainBoardService;
	@Autowired
	private DaangnMemberService memberService;
	
	@GetMapping(value = "/")
	public String list(Model model
					   , @PathVariable(value = "region", required = false) String region
					   , @PathVariable(value = "gu", required = false) String gu
					   , @PathVariable(value = "dong", required = false) String dong) {
		// model 에서 해당부분의 맞는 리스트만 list에 담아주자! 페이징 처리는 따로 하지 않겠다.
		return "list";
	}
}
