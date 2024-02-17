package kr.ezen.daangn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.ezen.daangn.service.ChatService;
import kr.ezen.daangn.vo.ChatMessageVO;
import kr.ezen.daangn.vo.ChatRoomVO;

@Controller
@RequestMapping("/chat")
public class ChatController {
	
	@Autowired
	private ChatService chatService;
	
	@GetMapping("/rooms")
	public String chatRooms(HttpSession session, Model model) {
		if(session.getAttribute("user") == null) { // 로그인을 하지 않은 경우
			return "redirect:/";
		}
		// 유저의 채팅방을 찾아 model에 넘겨준다
		
		return "chatRooms";
	}
	
	
	@GetMapping("/room/{chatRoomIdx}")
	public String chatRoom(HttpSession session, @PathVariable(value = "chatRoomIdx") int chatRoomIdx, Model model) {
		if(session.getAttribute("user") == null) { // 로그인을 하지 않은 경우
			return "redirect:/";
		}
		// 또한 chatRoom이 가지는 유저의 idx가 아닌경우
		if(true) {
			return "redirect:/";
		}
		// 상대 유저와의 체팅내용을 넘겨준다
		
		return "chatRoom";
	}
	
	@GetMapping("/createChatRoom")
	public String creathChatRoomGet(HttpSession session) {
		if(session.getAttribute("user") == null) { // 로그인을 하지 않은 경우
			return "redirect:/";
		} else {
			session.removeAttribute("user");
			return "redirect:/";
		}
	}
	@PostMapping("/createChatRoom")
	@ResponseBody
	public String creathChatRoomPost(HttpSession session, ChatRoomVO chatRoomVO) {
		// 1. 체팅방이 있는지 확인한다. (없으면 만들고 숫자를 넘기고 그 주소로가자, 있으면 숫자를 넘기고 그 주소로 가자)
		
		return "";
	}
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageVO addUser(@Payload ChatMessageVO chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
    
    @MessageMapping("/chat/{chatRoomId}")
    public void sendMessage(@Payload ChatMessageVO chatMessageVO, @DestinationVariable Long chatRoomId) {
    	chatService.sendMessage(chatMessageVO, chatRoomId);
    }
}
