package kr.ezen.daangn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *  생활꿀팁의 컨트롤러
 */
@Controller
@RequestMapping("/smartLivingTips")
public class SmartLivingTipsController {
	
	@GetMapping(value = {"","/"})
	public String index(Model model) {
		return "smartLivingTipsIndex";
	}
}
