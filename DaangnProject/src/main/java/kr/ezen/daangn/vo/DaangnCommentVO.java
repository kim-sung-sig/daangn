package kr.ezen.daangn.vo;

import java.util.Date;

import lombok.Data;

@Data
public class DaangnCommentVO {
	private int idx;			// 키필드
	private int userIdx;		// 어떤 유저에 대하여
	private int writerIdx;		// 어떤 유저가
	private int score;			// 총점?				// 유저랑 연결! 0~50;
	private String content;		// 댓글
	private Date regDate;		// 댓글쓴 날짜
}
