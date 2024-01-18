package kr.ezen.daangn.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import kr.ezen.daangn.service.BoardService;
import kr.ezen.daangn.vo.BoardVO;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.PagingVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	
	@Autowired
	BoardService boardService;

	@GetMapping(value = { "/", "/main", "/index" })
	public String home(Model model, @RequestParam(value = "search", required = false) String search,
			@ModelAttribute(value = "commonVO") CommonVO commonVO) {
		PagingVO<BoardVO> pv = boardService.selectList(commonVO);
		model.addAttribute("pv", pv);
		return "index";
	}

	@GetMapping("/upload")
	public String upload() {
		return "upload";
	}
	
	@PostMapping("/upload/imageUpload")
	@ResponseBody
	public String imageUpload(HttpServletRequest request, @RequestPart(value = "file") MultipartFile file) { // MultipartFile 이것이 파일을 알아서 받아준다.
		// --------------------------------------------------------------------------------------
		// 서버의 업로드될 경로 확인
		String uploadPath = request.getServletContext().getRealPath("/summernote/");
		// 파일 객체 생성
		File file2 = new File(uploadPath);
		// 폴더가 없다면 폴더를 생성해준다.
		if (!file2.exists()) {
			file2.mkdirs();
		}
		log.info("서버 실제 경로 : " + uploadPath);
		// --------------------------------------------------------------------------------------
		String saveName="";
		// 파일받기 : 파일은 MultipartFile로 구분해서 받아준다.
		if (file != null && file.getSize() > 0) { // 파일이 넘어왔다면
			try {
				// 저장파일의 이름 중복을 피하기 위해 저장파일이름을 유일하게 만들어 준다.
				String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
				// 파일 객체를 만들어 저장해 준다.
				File saveFile = new File(uploadPath, saveFileName);
				// 파일 복사
				FileCopyUtils.copy(file.getBytes(), saveFile);
				saveName = request.getContextPath() + "/summernote/" + saveFileName;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return saveName;
	}	
	//---------------------------------------------------------------------------------------------------------------
	/// GET방식 접근 방지
	@GetMapping(value = "/uploadOk")
	public String upload2Get(HttpServletRequest request) {
		return "redirect:/form2";
	}

	@PostMapping(value = "/uploadOk")
	public String upload2Post(MultipartHttpServletRequest request, Model model) {
		// MultipartFile이 파일을 알아서 받아준다!

		// 서버의 업로드될 경로를 가져온다!
		String uploadPath = request.getServletContext().getRealPath("./upload/");
		// 파일생성
		File file2 = new File(uploadPath);
		if (!file2.exists()) {
			file2.mkdirs();
		}
		log.info("서버 실제 경로 : " + uploadPath); // 확인용
		// --------------------------------------------------------------------------------------
		// 여러개 파일 받기
		List<MultipartFile> list = request.getFiles("file"); // form에 있는 name과 일치 // 배열로 받아준다!
		try {
			// 받은 파일들의 정보를 저장할 변수를 만들자
			StringBuffer sb = new StringBuffer();
			if (list != null && list.size() > 0) { // 받은 파일이 존재한다면
				// 반복해서 받는다.
				for (MultipartFile file : list) {
					// 파일이 없으면 처리하지 않는다.
					if (file != null && file.getSize() > 0) {
						// 저장파일의 이름 중복을 피하기 위해 저장파일이름을 유일하게 만들어 준다.
						String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
						// 파일 객체를 만들어 저장해 준다.
						File saveFile = new File(uploadPath, saveFileName);
						// 파일 복사
						FileCopyUtils.copy(file.getBytes(), saveFile); // 실제 서버 경로에 저장을 해놓고
						
						// db에도 저장하자!
						
						sb.append("saveName : " + saveFileName + "<br>");
						sb.append("originalName : " + file.getOriginalFilename() + "<br>");
						sb.append("contentType : " + file.getContentType() + "<br>");
						sb.append("fileSize : " + file.getSize() + "<br>");
						sb.append("<button onclick=\"fileDown('" + file.getOriginalFilename() + "','" + saveFileName
								+ "');\">" + file.getOriginalFilename() + "</button><br>");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "result2";
	}

}
