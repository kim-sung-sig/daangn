package kr.ezen.daangn.vo;

import lombok.Data;

@Data
public class ChatRoomVO {
	private int roomIdx;
	private int userIdx;
	private int boardIdx;
	
	// 유저가 이 chatRoom을 삭제했는가?
	// boardIDx의 유저가 chatRoom을 삭제했는가?
	// 둘다 삭제했으면 쳇룸폭파
	
	// 추가하면 CRD가능
	
	// db설계
	private int boardUserIdx;
}
