package kr.ezen.daangn.vo;

import java.util.Date;

import lombok.Data;

/**
 * 상호작용을 전달하는 객체
 */
@Data
public class PopularVO {
	private int idx;
	private int boardRef;
	private int userRef;
	private int interaction;			// 상호작용 => 1:조회, 2:댓글, 3:좋아요 (점수부여 각각 1, 3, 6점)
	private Date interaction_time;		// 상호작용시간
	// db세팅
	
	private DaangnMemberVO member;
	private DaangnMainBoardVO board;
}

/*
CREATE TABLE tb_popular(
	idx NUMBER PRIMARY KEY,
	boardRef NUMBER NOT NULL,
	userRef NUMBER NOT NULL,
	interaction varchar2(10) NOT NULL,
	interaction_time TIMESTAMP DEFAULT sysdate,
	FOREIGN KEY (boardRef) REFERENCES jung_board(idx),
	FOREIGN KEY (userRef) REFERENCES jung_member(idx)
);
*/