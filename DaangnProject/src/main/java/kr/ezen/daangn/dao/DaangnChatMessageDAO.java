package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.ChatMessageVO;

@Mapper
public interface DaangnChatMessageDAO {
	
	/** 해당 하는 방의 채팅 가져오기
	 * (chatRoomIdx, lastItemIdx, sizeOfPage)
	 * 채팅방 번호, 마지막번호, 몇개씩
	 */
	List<ChatMessageVO> selectChatByChatRoomIdx(HashMap<String, Integer> map) throws SQLException;
	
	/** 가장큰 idx 얻기 */
	int getLastIdx() throws SQLException;
	
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
	 * 채팅방에 입장할때 채팅을 모두 읽음 표시하는 역할
	 * @throws SQLException
	 */
	void updateAllChat(ChatMessageVO chatMessage) throws SQLException;
	
	
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
