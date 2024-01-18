package kr.ezen.daangn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ezen.daangn.service.TestService;

@RestController
public class TestController {
	
	@Autowired
	TestService testService;
	
	
	@GetMapping("/test")
	public String test() {
		return testService.selectToday();
	}
}
