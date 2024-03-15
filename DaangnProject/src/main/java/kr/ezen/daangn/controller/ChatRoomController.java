package kr.ezen.daangn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.ChatService;
import kr.ezen.daangn.service.DaangnMainBoardService;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.vo.ChatMessageVO;
import kr.ezen.daangn.vo.ChatRoomVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import kr.ezen.daangn.vo.ScrollVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/chat")
public class ChatRoomController {
	
	@Autowired
	private ChatService chatService;
	@Autowired
	private DaangnMainBoardService daangnMainBoardService;
	@Autowired
	private DaangnMemberService daangnMemberService;
	
	
	// 나의 채팅에서 볼 것
	@GetMapping("/rooms")
	public String chatRooms(HttpSession session, Model model) {
		if(session.getAttribute("user") == null) { // 로그인을 하지 않은 경우
			return "redirect:/";
		}
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
		
		List<ChatRoomVO> chatList = chatService.selectChatRoomByUserIdx(memberVO.getIdx());
		// 유저의 채팅방을 찾아 model에 넘겨준다
		model.addAttribute("chatList", chatList);
		return "chat/chatRooms";
	}
	
	// 채팅 보기
	@GetMapping("/room/{chatRoomIdx}")
	public String chatRoom(HttpSession session, @PathVariable(value = "chatRoomIdx") int chatRoomIdx, Model model) {
		if(session.getAttribute("user") == null) { // 로그인을 하지 않은 경우
			return "redirect:/";
		}
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
		ChatRoomVO chatRoomVO = chatService.selectChatRoom(chatRoomIdx);
		DaangnMainBoardVO boardVO = daangnMainBoardService.selectByIdx(chatRoomVO.getBoardIdx());
		chatRoomVO.setBoardUserIdx(daangnMemberService.selectByIdx(boardVO.getUserRef()).getIdx());
		// 또한 chatRoom이 가지는 유저의 idx가 아닌경우
		if(memberVO.getIdx() != chatRoomVO.getUserIdx() && memberVO.getIdx() != chatRoomVO.getBoardUserIdx()) { // 채팅의 주인이 아닌경우
			return "redirect:/";
		}
		
		model.addAttribute("board", boardVO);
		model.addAttribute("chatRoomIdx", chatRoomIdx);
		model.addAttribute("sender", memberVO.getIdx());
		model.addAttribute("lastItemIdx", chatService.getChatMessageLastIdx());
		// model.addAttribute("list", chatService.selectMessageByChatRoomIdx(chatRoomIdx));
		return "chat/chatRoom";
	}
	
	@PostMapping("/findChatMessages")
	@ResponseBody
	public List<ChatMessageVO> findChatMessages(@RequestBody ScrollVO sv){
		log.info("findChatMessages 실행 {}", sv);
		List<ChatMessageVO> list = chatService.selectMessageByChatRoomIdx(sv.getChatRoomIdx(), sv.getLastItemIdx(), sv.getSizeOfPage());
		return list;
	}
	
	// 채팅방 조회 및 생성
	@GetMapping("/createChatRoom")
	public String creathChatRoomGet(HttpSession session) {
		return "redirect:/";
	}
	@PostMapping("/createChatRoom")
	@ResponseBody
	public String creathChatRoomPost(HttpSession session, @RequestBody ChatRoomVO chatRoomVO) {
		DaangnMemberVO memberVO = (DaangnMemberVO) session.getAttribute("user");
		if(memberVO == null) {
			return "0";
		}
		chatRoomVO.setUserIdx(memberVO.getIdx());
		log.info("chatRoom :{}", chatRoomVO);
		// 1. 체팅방이 있는지 확인한다. (없으면 만들고 숫자를 넘기고 그 주소로가자, 있으면 숫자를 넘기고 그 주소로 가자)
		int result = chatService.creatChatRoom(chatRoomVO);
		return ""+result;
	}
}
