package kr.ezen.daangn.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.ezen.daangn.service.ChatService;
import kr.ezen.daangn.service.DaangnMemberService;
import kr.ezen.daangn.vo.ChatMessageVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
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
		message.setRegDate(new Date());
		message.setNickName(memberVO.getNickName());
		if(memberVO.getUserFile() != null) {
			message.setUserProfileName(memberVO.getUserFile().getSaveFileName());			
		}
		log.info("message : {}", message);
		// 메시지 저장
		if(message.getTypeRef() != 1) {
			chatService.insertMessage(message);
			chatService.updateChatRoomLastUpdateDate(message.getChatRoom());
		}
		// 메시지 보내기
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getChatRoom(), message);
    }
	
	
	@PutMapping("/chat/read")
	@ResponseBody
	public void messageUpdateReadCount(@RequestBody ChatMessageVO chatMessageVO) {
		log.info("messageUpdateReadCount실행 : {}", chatMessageVO);
		chatService.updateReadCount(chatMessageVO.getIdx());
	}
	@PutMapping("/chat/readAll")
	@ResponseBody
	public void messageUpdateReadCountAll(@RequestBody ChatMessageVO chatMessageVO) {
		log.info("messageUpdateReadCountAll 실행 : {}", chatMessageVO);
		chatService.updateReadCountAll(chatMessageVO.getChatRoom(), chatMessageVO.getSender());
	}
	
	@GetMapping("/chat/get")
	@ResponseBody
	public ChatMessageVO getChatMessage(@RequestParam(value = "idx", required = true) int idx) {
		ChatMessageVO messageVO = chatService.selectMessageByIdx(idx);
		return messageVO;
	}
}
