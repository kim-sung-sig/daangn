package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.ChatMessageVO;

@Mapper
public interface DaangnChatMessageDAO {
	
	// 1. 해당 하는 방의 채팅 가져오기
	List<ChatMessageVO> selectChatByChatRoomIdx(HashMap<String, Integer> map) throws SQLException;
	
	// 2. 해당 하는 방의 채팅 전체 갯수 가져오기
	int selectChatCountByChatRoomIdx(int chatRoomIdx) throws SQLException;
	
	// 2. 글 저장하기
	void insertChat(ChatMessageVO chatMessage) throws SQLException;
	
	// 3. 읽음 표시하기!
	void updateChat(ChatMessageVO chatMessage) throws SQLException;
	
	// 4. 안읽은 갯수가져오기
	int unreadCount(ChatMessageVO chatMessage) throws SQLException;
	
	// 3. 글 삭제하기
	void deleteChat(int idx) throws SQLException;
	
}
