package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.ChatMessageVO;

@Mapper
public interface DaangnChatMessageDAO {
	// 페이징 하지말자 복잡하다.
	// 1. 해당 하는 방의 채팅 가져오기
	List<ChatMessageVO> selectChatByChatRoomIdx(HashMap<String, Integer> map) throws SQLException;
	
	// 2. 해당 하는 방의 채팅 전체 갯수 가져오기
	int selectChatCountByChatRoomIdx(int chatRoomIdx) throws SQLException;
	
	/**
	 * 글 저장하기
	 * @param chatMessage
	 * @throws SQLException
	 */
	void insertChat(ChatMessageVO chatMessage) throws SQLException;
	
	/**
	 * readed 읽음 표시하기
	 * @param idx
	 * @throws SQLException
	 */
	void updateChat(int idx) throws SQLException;
	
	/**
	 * 안읽은 갯수가져오기
	 * @param idx
	 * @throws SQLException
	 */
	int unreadCount(ChatMessageVO chatMessage) throws SQLException;
	
	/**
	 * 한개얻기
	 * @param idx
	 * @return ChatMessageVO
	 * @throws SQLException
	 */
	ChatMessageVO selectByIdx(int idx) throws SQLException;
	
	// 3. 글 삭제하기
	void deleteChat(int idx) throws SQLException;
	
}
