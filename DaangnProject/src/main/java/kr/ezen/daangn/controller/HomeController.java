package kr.ezen.daangn.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.DaangnBoardFileService;
import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.vo.DaangnFileVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
@Configuration
public class HomeController {
	
	@GetMapping(value = { "/", "/main", "/index" })
	public String home(HttpServletRequest request) {
		return "index";
	}
	//===========================================================================================
	
	// 글작성
	@GetMapping(value = "/upload")
	public String upload(HttpServletRequest request) {
		return "upload";
	}
	@GetMapping("/uploadOk")
	public String uploadOkGet() {
		return "redirect:/";
	}
	
	// 보드
	@Autowired
	private DaangnMainBoardService daangnMainBoardService;
	// 파일
	@Autowired
	private DaangnBoardFileService daangnBoardFileService;
	
	@PostMapping("/uploadOk")
	@Transactional
	public String uploadOk(HttpSession session,MultipartHttpServletRequest request, @ModelAttribute(value = "boardVO") DaangnMainBoardVO boardVO) {
		log.info("uploadOk 실행 : {}", boardVO);
		// board save
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
		boardVO.setRef(memberVO.getIdx());
		daangnMainBoardService.saveMainBoard(boardVO); // 저장된 idx_seq를 리턴한다!
		int boardIdx = boardVO.getIdx();
		log.info("boardIdx = {}", boardIdx);
		// file save
		String uploadPath = request.getServletContext().getRealPath("./files/");
		// 파일 객체 생성
		File file2 = new File(uploadPath);
		// 폴더가 없다면 폴더를 생성해준다.
		if (!file2.exists()) {
			file2.mkdirs();
		}
		log.info("서버 실제 경로 : " + uploadPath);
		// --------------------------------------------------------------------------------------
		List<MultipartFile> list = request.getFiles("file"); // form에 있는 name과 일치
		try {
			if (list != null && list.size() > 0) {
				for(MultipartFile file :list) {
					if(file != null && file.getSize() >0) {
						String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
						String originFileName = file.getOriginalFilename();
						File savaFile = new File(uploadPath, saveFileName);
						FileCopyUtils.copy(file.getBytes(), savaFile);
						
						DaangnFileVO fileVO = new DaangnFileVO();
						fileVO.setRef(boardIdx);
						fileVO.setSaveFileName(saveFileName);
						fileVO.setOriginFileName(originFileName);
						log.info("fileVO : {}", fileVO);
						daangnBoardFileService.insert(fileVO);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "uploadOk";
	}
	
	// 딱! 한번만 실행해야한다!
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
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
