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
    
    /**
     * 조회
     * 채팅방 사용가능 유저가 누구인지 리턴
     * @param chatRoomIdx
     * @return
     */
    public ChatRoomVO selectChatRoom(int chatRoomIdx){
    	ChatRoomVO ChatRoom = null;
    	try {
    		ChatRoom = daangnChatRoomDAO.selectChatRoomByIdx(chatRoomIdx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return ChatRoom;
    }
    
    /**
     * 조회
     * 채팅방을 만들때 이미 이전 채팅방이 있는 경우 채팅방 번호 리턴
     * 채팅방이 없는 경우 생성해서 chatRoomIdx 리턴
     * @param chatRoomVO
     * @return
     */
    public int creatChatRoom(ChatRoomVO chatRoomVO) {
    	int result = 0;
    	try {
    		if(daangnChatRoomDAO.findChatRoom(chatRoomVO) == 0) { // 없으면 만들기!
    			daangnChatRoomDAO.createChatRoom(chatRoomVO);    			
    			result = chatRoomVO.getRoomIdx();
    		} else { // 있으면 roomIdx넘겨주기
    			ChatRoomVO dbChatRoomVO = daangnChatRoomDAO.selectChatRoom(chatRoomVO);
    			result = dbChatRoomVO.getRoomIdx();
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * 조회 사용자의 idx로 사용자가 속한 ChatRoom을 리턴
     * @param userIdx
     * @return
     */
    public List<ChatRoomVO> selectChatRoomByUserIdx(int userIdx){
    	List<ChatRoomVO> list = null;
    	try {
			list = daangnChatRoomDAO.selectChatRoomByUserIdx(userIdx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return list;
    }
    
    /**
     * ChatRoomIdx에 해당하는 Message들을 리턴하는 메서드 >> 이것도 나중에 페이징 처리해야함
     * @param chatRoomIdx
     * @return
     */
    public List<ChatMessageVO> selectMessageByChatRoomIdx(int chatRoomIdx){
    	List<ChatMessageVO> list = null;
    	try {
			list = daangnChatMessageDAO.selectChatByChatRoomIdx(chatRoomIdx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return list;
    }
    
    /**
     * 저장
     * 날라온 ChatMessage를 db에 저장
     * @param chatMessageVO
     */
    public void insertMessage(ChatMessageVO chatMessageVO) {
    	insertMessage(chatMessageVO);
    }
	
    // 5. 채팅방 삭제하기?? 이건 고민해봐야될듯
	
}
