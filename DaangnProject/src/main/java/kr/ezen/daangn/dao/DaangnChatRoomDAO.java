package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.ChatRoomVO;

@Mapper
public interface DaangnChatRoomDAO {
	
	// 0. 채팅방 조회
	ChatRoomVO selectChatRoom(int chatRoomIdx) throws SQLException;
    
	// 1. 채팅방 생성(기본키 리턴)
    void createChatRoom(ChatRoomVO chatRoom) throws SQLException;
    
    // 2. 채팅방 조회!
    List<ChatRoomVO> selectChatRoomByUserIdx(int userIdx) throws SQLException;
    
    // 3. 채팅방 삭제
    void deleteChatRoom(long roomIdx) throws SQLException;
    
    // 4. 채팅방 있는지 없는지 확인!(idx 리턴)
    int findChatRoom(ChatRoomVO chatRoom) throws SQLException;
    
}
