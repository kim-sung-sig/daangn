package kr.ezen.daangn.vo;

import lombok.Data;

@Data
public class ChatRoomVO {
	private int roomIdx;
	private int userIdx;
	private int boardIdx;
	
	private int boardUserIdx;
}
