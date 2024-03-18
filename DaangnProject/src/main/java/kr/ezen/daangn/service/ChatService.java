package kr.ezen.daangn.service;

import java.util.List;

import kr.ezen.daangn.vo.ChatMessageVO;
import kr.ezen.daangn.vo.ChatRoomVO;
import kr.ezen.daangn.vo.DaangnMemberVO;

public interface ChatService {
	// 키로 찾기
	ChatRoomVO selectChatRoom(int chatRoomIdx);
	// 채팅생성 후 키 리턴
	int creatChatRoom(ChatRoomVO chatRoomVO);
	// 유저가 속한 채팅방 리턴
	List<ChatRoomVO> selectChatRoomByUserIdx(int userIdx);
	// 게시글에 해당하는 채팅방 유저 리턴
	List<DaangnMemberVO> selectChatRoomByBoardIdx(int boardIdx);
	// userIdx에 해당하는 안읽은 메시지 갯수 리턴
	int selectUnReadCountByUserIdx(int userIdx);
	// ChatRoomIdx에 해당하는 Message페이징 리턴하는 메서드
	List<ChatMessageVO> selectMessageByChatRoomIdx(int chatRoomIdx, int lastItemIdx, int sizeOfPage);
	// 최대idx 가져오기
	int getChatMessageLastIdx();
	// 저장
	void insertMessage(ChatMessageVO chatMessageVO);
	// 읽음처리
	void updateReadCount(int idx);
	// 일괄읽음처리
	void updateReadCountAll(int chatRoomIdx, int sender);
	// 채팅방 LastUpdateDate 수정
	void updateChatRoomLastUpdateDate(int roomIdx);
}
