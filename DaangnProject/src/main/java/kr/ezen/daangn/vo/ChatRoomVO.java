package kr.ezen.daangn.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRoomVO {
	private long roomIdx;
	private long user1; // 이 두값을 정렬된 상태로 넘기겟다 user1 < user2
	private long user2;

}
