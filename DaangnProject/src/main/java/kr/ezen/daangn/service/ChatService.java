package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ezen.daangn.dao.DaangnChatMessageDAO;
import kr.ezen.daangn.dao.DaangnChatRoomDAO;
import kr.ezen.daangn.dao.DaangnMemberDAO;
import kr.ezen.daangn.vo.ChatMessageVO;
import kr.ezen.daangn.vo.ChatRoomVO;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.PagingVO;

@Service(value = "chatService")
public class ChatService {
	
    @Autowired
    private DaangnChatRoomDAO daangnChatRoomDAO;
    
    @Autowired
    private DaangnChatMessageDAO daangnChatMessageDAO;
    
    @Autowired
    private DaangnMemberDAO daangnMemberDAO;
    
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
    public PagingVO<ChatMessageVO> selectMessageByChatRoomIdx(int chatRoomIdx, CommonVO cv){
    	PagingVO<ChatMessageVO> pv = null;
    	try {
    		HashMap<String, Integer> map = new HashMap<>();
    		int totalCount = daangnChatMessageDAO.selectChatCountByChatRoomIdx(chatRoomIdx);
    		map.put("chatRoomIdx", chatRoomIdx);
    		pv = new PagingVO<>(totalCount, cv.getCurrentPage(), cv.getSizeOfPage(), cv.getSizeOfBlock());
    		map.put("startNo", pv.getStartNo());
    		map.put("endNo", pv.getEndNo());
			List<ChatMessageVO> messageList = daangnChatMessageDAO.selectChatByChatRoomIdx(map);
			for(ChatMessageVO cm : messageList) {
				cm.setNickName(daangnMemberDAO.selectByIdx(cm.getSender()).getNickName());
			}
			pv.setList(messageList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return pv;
    }
    
    /**
     * 저장
     * 날라온 ChatMessage를 db에 저장
     * @param chatMessageVO
     */
    public void insertMessage(ChatMessageVO chatMessageVO) {
    	try {
			daangnChatMessageDAO.insertChat(chatMessageVO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 수정
     * 날라온 idx로 readed--;
     * @param idx
     */
    public void updateReadCount(int idx) {
    	try {
			daangnChatMessageDAO.updateChat(idx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    public ChatMessageVO selectMessageByIdx(int idx) {
    	ChatMessageVO messageVO = null;
    	try {
    		messageVO = daangnChatMessageDAO.selectByIdx(idx);
    		messageVO.setNickName(daangnMemberDAO.selectByIdx(messageVO.getSender()).getNickName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return messageVO;
    }
	
    // 5. 채팅방 삭제하기?? 이건 고민해봐야될듯
	
}
