package kr.ezen.daangn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import kr.ezen.daangn.vo.MessageVO;

@Service
public class ChatMessageService {
	@Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendMessage(MessageVO chatMessage, Long chatRoomId) {
        messagingTemplate.convertAndSend("/topic/chatRoom/" + chatRoomId, chatMessage);
    }
}
