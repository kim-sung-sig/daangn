package kr.ezen.daangn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.ezen.daangn.service.NewsService;
import kr.ezen.daangn.vo.RssVO.Item;



@Controller
@RequestMapping("/news")
public class NewsController {
	
	@Autowired
	private NewsService newsService;
	
	@GetMapping(value = {"","/"})
	public String newsMaing(Model model) {
		return "news/main";
	}
	
	@GetMapping(value = "/all-news")
	public String allNews(Model model) {
		List<Item> newsList = newsService.getNewsByUrl("https://www.hankyung.com/feed/all-news");
		model.addAttribute("items", newsList);
		model.addAttribute("title","전체 뉴스");
		return "news/views";
	}
	@GetMapping(value = "/it")
	public String it(Model model) {
		List<Item> newsList = newsService.getNewsByUrl("https://www.hankyung.com/feed/it");
		model.addAttribute("items", newsList);
		model.addAttribute("title","IT 뉴스");
		return "news/views";
	}
	@GetMapping(value = "/economy")
	public String economy(Model model) {
		List<Item> newsList = newsService.getNewsByUrl("https://www.hankyung.com/feed/economy");
		model.addAttribute("items", newsList);
		model.addAttribute("title","경제 뉴스");
		return "news/views";
	}
	@GetMapping(value = "/finance")
	public String finance(Model model) {
		List<Item> newsList = newsService.getNewsByUrl("https://www.hankyung.com/feed/finance");
		model.addAttribute("items", newsList);
		model.addAttribute("title","증권 뉴스");
		return "news/views";
	}
	@GetMapping(value = "/realetate")
	public String realetate(Model model) {
		List<Item> newsList = newsService.getNewsByUrl("https://www.hankyung.com/feed/realestate");
		model.addAttribute("items", newsList);
		model.addAttribute("title","부동산 뉴스");
		return "news/views";
	}
	@GetMapping(value = "/entertainment")
	public String entertainment(Model model) {
		List<Item> newsList = newsService.getNewsByUrl("https://www.hankyung.com/feed/entertainment");
		model.addAttribute("items", newsList);
		model.addAttribute("title","연예 뉴스");
		return "news/views";
	}
	@GetMapping(value = "/life")
	public String life(Model model) {
		List<Item> newsList = newsService.getNewsByUrl("https://www.hankyung.com/feed/life");
		model.addAttribute("items", newsList);
		model.addAttribute("title","생활 뉴스");
		return "news/views";
	}
	@GetMapping(value = "/sports")
	public String sports(Model model) {
		List<Item> newsList = newsService.getNewsByUrl("https://www.hankyung.com/feed/sports");
		model.addAttribute("items", newsList);
		model.addAttribute("title","스포츠 뉴스");
		return "news/views";
	}
	@GetMapping(value = "/international")
	public String international(Model model) {
		List<Item> newsList = newsService.getNewsByUrl("https://www.hankyung.com/feed/international");
		model.addAttribute("items", newsList);
		model.addAttribute("title","국제 뉴스");
		return "news/views";
	}
	@GetMapping(value = "/society")
	public String society(Model model) {
		List<Item> newsList = newsService.getNewsByUrl("https://www.hankyung.com/feed/society");
		model.addAttribute("items", newsList);
		model.addAttribute("title","사회 뉴스");
		return "news/views";
	}
	@GetMapping(value = "/politics")
	public String politics(Model model) {
		List<Item> newsList = newsService.getNewsByUrl("https://www.hankyung.com/feed/politics");
		model.addAttribute("items", newsList);
		model.addAttribute("title","정치 뉴스");
		return "news/views";
	}
	
}
