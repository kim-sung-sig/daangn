package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.List;

import kr.ezen.daangn.vo.ChatMessageVO;

public interface DaangnChatMessageDAO {
	
	// 1. 해당 하는 방의 모든 채팅 가져오기
	List<ChatMessageVO> selectChatByChatRoomIdx(int chatRoomIdx) throws SQLException;
	// 2. 글 저장하기
	void insertChat(ChatMessageVO chatMessageVO) throws SQLException;
	// 3. 글 삭제하기
	void deleteChat(int idx) throws SQLException;
	
}
