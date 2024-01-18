package kr.ezen.daangn.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import kr.ezen.daangn.vo.Message;

@Controller
public class MessageController {
	
	@MessageMapping("/chat")
	@SendTo("/topic/messages")
	public Message sendMessage(Message message) {
		return message;
	}
}
