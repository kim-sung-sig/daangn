package kr.ezen.daangn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
	
	@GetMapping(value = { "/", "/main", "/index" })
	public String home(HttpServletRequest request) {
		return "index";
	}
	//===========================================================================================
	
// 딱! 한번만 실행해야한다!
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
//	
//	@Autowired
//	private BCryptPasswordEncoder passwordEncoder;
	
//	@GetMapping("/dbinit") // 기존에 등록된 비번을 암호화 해서 변경한다. 1번만 실행하고 지워줘라~~~
//	public String dbInit() {
//		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"admin");
//		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"master");
//		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"webmaster");
//		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"root");
//		jdbcTemplate.update("update daangn_member set password=? where username=?", passwordEncoder.encode("123456"),"dba");
//		return "redirect:/";
//	}
		
}
