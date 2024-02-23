package kr.ezen.daangn.vo;

import java.util.Date;

import lombok.Data;

@Data
public class DaangnCommentVO {
	private int idx;			// 키필드
	private int userIdx;		// 어떤 유저에 대하여
	private int writerIdx;		// 어떤 유저가
	private int eval;			// 총점?				// 유저랑 연결! 12345 별중 -3 -1 0 1 3 으로 온도 변경하자
	private String content;		// 댓글
	private Date regDate;		// 댓글쓴 날짜
}
