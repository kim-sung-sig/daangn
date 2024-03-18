package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.ChatRoomVO;

@Mapper
public interface DaangnChatRoomDAO {
	
	/**
	 * roomIdx 로 채팅방 조회
	 * @param roomIdx
	 * @return ChatRoomVO
	 * @throws SQLException
	 */
	ChatRoomVO selectChatRoomByIdx(int roomIdx) throws SQLException;
	
	/**
	 * 채팅방 생성
	 * @param chatRoom
	 * @throws SQLException
	 */
    void createChatRoom(ChatRoomVO chatRoom) throws SQLException;
    
    /**
     * userIdx에 해당하는 채팅방 리스트 얻기
     * @param userIdx
     * @return List<ChatRoomVO>
     * @throws SQLException
     */
    List<ChatRoomVO> selectChatRoomByUserIdx(int userIdx) throws SQLException;
    
    /**
     * boardIdx에 해당하는 채팅방 리스트 얻기
     * @param boardIdx
     * @return List<ChatRoomVO>
     * @throws SQLException
     */
    List<ChatRoomVO> selectChatRoomByBoardIdx(int boardIdx) throws SQLException;
    
    /**
     * 게시글에 해당하는 채팅수 얻기
     * @param boardIdx
     * @return 게시글에 해당하는 채팅수
     * @throws SQLException
     */
    int selectCountByBoardIdx(int boardIdx) throws SQLException;
    
    /**
     * 채팅방이 있는지 없는지 판단 있으면 ChatRoomVO의 idx 없으면 0
     * @param chatRoom
     * @return ChatRoomVO의 idx 없으면 0
     * @throws SQLException
     */
    int findChatRoom(ChatRoomVO chatRoom) throws SQLException;
    
    /**
     * roomIdx로 lastUpdateDate를 업데이트
     * @param roomIdx
     * @throws SQLException
     */
    void updateLastUpdateDate(int roomIdx) throws SQLException;
}
