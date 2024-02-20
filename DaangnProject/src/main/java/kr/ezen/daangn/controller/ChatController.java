package kr.ezen.daangn.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import kr.ezen.daangn.service.ChatService;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.vo.ChatMessageVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChatController {
	
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private DaangnMemberService daangnMemberService;
	
	@MessageMapping("/chat/message")
    public void message(ChatMessageVO message) {
		DaangnMemberVO memberVO = daangnMemberService.selectByIdx(message.getSender());
		message.setNickName(memberVO.getNickName());
		message.setRegDate(new Date());
		log.info("message : {}", message);
		// 메시지 보내기
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getChatRoom(), message);
        // 메시지 저장
        // chatService.insertMessage(message);
    }
}
