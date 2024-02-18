package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ezen.daangn.dao.DaangnChatMessageDAO;
import kr.ezen.daangn.dao.DaangnChatRoomDAO;
import kr.ezen.daangn.vo.ChatMessageVO;
import kr.ezen.daangn.vo.ChatRoomVO;

@Service(value = "chatService")
public class ChatService {
	
    @Autowired
    private DaangnChatRoomDAO daangnChatRoomDAO;
    
    @Autowired
    private DaangnChatMessageDAO daangnChatMessageDAO;
    
    // 0. 채팅방idx로 누가누가 사용가능한지 알려주기
    public ChatRoomVO selectChatRoom(int chatRoomIdx){
    	ChatRoomVO ChatRoom = null;
    	try {
    		ChatRoom = daangnChatRoomDAO.selectChatRoom(chatRoomIdx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return ChatRoom;
    }
    
    // 1. 체팅방 만들기
    public long creatChatRoom(ChatRoomVO chatRoomVO) {
    	long result = 0;
    	try {
    		if(daangnChatRoomDAO.findChatRoom(chatRoomVO) == 0) { // 없으면 만들기!
    			daangnChatRoomDAO.createChatRoom(chatRoomVO);    			
    			result = chatRoomVO.getRoomIdx();
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    // 2. 채팅방리스트 보기
    // -1. 안읽은 글 날리기???
    public List<ChatRoomVO> selectChatRoomByUserIdx(int userIdx){
    	List<ChatRoomVO> list = null;
    	try {
			list = daangnChatRoomDAO.selectChatRoomByUserIdx(userIdx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return list;
    }
    
    // 3. 채팅방 보기 (체팅내역을 날려주기)
    // -1. 읽음표시하기?
    public List<ChatMessageVO> selectMessageByChatRoomIdx(int chatRoomIdx){
    	List<ChatMessageVO> list = null;
    	try {
			list = daangnChatMessageDAO.selectChatByChatRoomIdx(chatRoomIdx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return list;
    }
    // 4. 메시지 저장하기
    public void insertMessage(ChatMessageVO chatMessageVO) {
    	insertMessage(chatMessageVO);
    }
	
    // 5. 채팅방 삭제하기?? 이건 고민해봐야될듯
	
}
