package kr.ezen.daangn.vo;

import java.util.Date;

import lombok.Data;

@Data
public class ChatMessageVO {
	private int idx;			// 키필드
	private int chatRoom;		// chatRoomNum
	private int sender;		// 보내는 이
	private String content;		// 내용
	private Date regDate;		// 보낸 시간
	private int readed;			// 읽었는지 안읽었는지 체크! 1 안읽음 0 읽음
	
	
	private ChatRoomVO chatRoomVO;			// 여기에서 boardIdx를 이용해 board를 구하고 board의 ref를 이용해 ??
	private String nickName;	// 보낸이의 정보를 담을 vo
}
