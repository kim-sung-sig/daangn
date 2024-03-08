package kr.ezen.daangn.vo;

import java.util.Date;

import lombok.Data;

/**
 * 쳇메시지를 저장할 객체
 * insert select delete가능
 * delete는 추가 고민필요!
 */
@Data
public class ChatMessageVO {
	
	private int idx;			// 키필드
	private int chatRoom;		// chatRoomNum
	private int typeRef;		// 1: ENTER, 2: TALK, 3: RESERVE
	private int sender;			// 보내는 이
	private String content;		// 내용
	private Date regDate;		// 보낸 시간
	
	private int readed;
	// db 설계 끝
	
	private ChatRoomVO chatRoomVO;			// 여기에서 boardIdx를 이용해 board를 구하고 board의 ref를 이용해 ??
	private String typeStr;					// ENTER, TALK, RESERVE   3개로만 구성하겠다.
	private String nickName;				// 보낸이의 정보를 담을 vo 나중에 member로 교체 할 것임
	
}
