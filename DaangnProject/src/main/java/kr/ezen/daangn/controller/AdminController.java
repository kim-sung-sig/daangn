package kr.ezen.daangn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.vo.DaangnMemberVO;

@Controller
@RequestMapping(value = "/adm")
public class AdminController {
	
	@GetMapping(value = {"","/"})
	public String index(HttpSession session, Model model) {
		if(session.getAttribute("user") == null) {
    		return "redirect:/";
    	}
    	DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
    	if(!memberVO.getRole().equals("ROLE_ADMIN")) {
    		return "redirect:/";
    	}
    	model.addAttribute("name", memberVO.getName());
    	
    	return "admin/index"; // 관리자 메인페이지
	}
}
