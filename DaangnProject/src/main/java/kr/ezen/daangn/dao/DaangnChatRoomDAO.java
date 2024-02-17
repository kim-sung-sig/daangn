package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.List;

import kr.ezen.daangn.vo.ChatRoomVO;

public interface DaangnChatRoomDAO {
	
    // 1. 채팅방 생성
    void createChatRoom(ChatRoomVO chatRoom) throws SQLException;
    
    // 2. 채팅방 조회!
    List<ChatRoomVO> selectChatRoomByUseridx(int useridx) throws SQLException;
    
    // 3. 채팅방 삭제
    void deleteChatRoom(long roomIdx) throws SQLException;
    
}
