package kr.ezen.daangn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import kr.ezen.daangn.service.ChatService;
import kr.ezen.daangn.vo.ChatMessageVO;

@Controller
public class ChatController {
	
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private ChatService chatService;
	
	@MessageMapping("/chat/message")
    public void message(ChatMessageVO message) {		
		// 메시지 보내기
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getChatRoom(), message);
        // 메시지 저장
        chatService.insertMessage(message);
    }
}
