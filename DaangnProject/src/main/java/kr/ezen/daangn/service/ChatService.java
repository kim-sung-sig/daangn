package kr.ezen.daangn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import kr.ezen.daangn.dao.DaangnChatMessageDAO;
import kr.ezen.daangn.dao.DaangnChatRoomDAO;
import kr.ezen.daangn.vo.ChatMessageVO;

@Service
public class ChatService {
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	
    @Autowired
    private DaangnChatRoomDAO chatRoomDAO;
    
    @Autowired
    private DaangnChatMessageDAO chatMessageDAO;
    // 0. 체팅방 만들기
    
    // 1. 채팅방리스트 보기
    
    // 2. 채팅방 보기 (체팅내역을 날려주기)
    
    // 3. 메시지 저장하기 및 전송
	public void sendMessage(@NonNull ChatMessageVO chatMessage, Long chatRoomId) {
        messagingTemplate.convertAndSend("/topic/chatRoom/" + chatRoomId, chatMessage);
    }
	
    // 4. 채팅방 삭제하기?? 이건 고민해봐야될듯
}
