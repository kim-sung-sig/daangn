package kr.ezen.daangn.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRoomVO {
	private int roomIdx;
	private int userIdx;
	private int boardIdx;
	
	private int boardUserIdx;
}
