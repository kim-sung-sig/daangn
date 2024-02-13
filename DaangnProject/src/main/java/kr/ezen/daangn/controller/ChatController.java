package kr.ezen.daangn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import kr.ezen.daangn.service.ChatMessageService;
import kr.ezen.daangn.vo.MessageVO;

@Controller
public class ChatController {
	
	@Autowired
	private ChatMessageService chatMessageService;
	

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public MessageVO addUser(@Payload MessageVO chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
    
    @MessageMapping("/chat/{chatRoomId}")
    public void sendMessage(@Payload MessageVO chatMessage, @DestinationVariable Long chatRoomId) {
        chatMessageService.sendMessage(chatMessage, chatRoomId);
    }
}
