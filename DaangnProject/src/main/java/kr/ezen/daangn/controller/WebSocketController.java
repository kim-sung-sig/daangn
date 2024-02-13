package kr.ezen.daangn.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Configuration
@Controller
public class WebSocketController {
	
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public String handleMessage(@Payload String message) {
        return message;
    }
}
