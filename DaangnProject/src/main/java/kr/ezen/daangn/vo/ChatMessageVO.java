package kr.ezen.daangn.vo;

import java.util.Date;

import lombok.Data;

@Data
public class ChatMessageVO {
	private int idx;			// 키필드
	private int chatRoom;		// chatRoomNum
	private int sender;			// 보내는 이
	private String content;		// 내용
	private Date regDate;		// 보낸 시간
}
