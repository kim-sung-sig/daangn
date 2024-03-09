package kr.ezen.daangn.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ChatRoomVO {
	private int roomIdx;
	private int userIdx;
	private int boardIdx;
	private int boardUserIdx;
	private Date lastUpdateDate;
	// db설계
	
	private DaangnMainBoardVO board;
	private DaangnMemberVO member;
	private List<ChatMessageVO> messageList;
	private int unreadCount;
}
