package kr.ezen.daangn.vo;

import java.util.Date;
import java.util.List;

public class DaangnSmartLivingTipVO {
	private int idx;					// 키필드
	private int userIdx;				// user>idx
	
	private String title;				// 제목
	private String content;				// 내용
	
	private int readCount;				// 조회수
	private Date regDate;				// 게시일
	private int	deleted;				// 게시글 보이기 0 안보이기 1
	// db설계끝
	
	
	// 좋아요!
	private int countLike;				// 좋아요 수
	private int commentCount;			// 댓글 수

	// 제한된 유저정보만 idx, 이름(nickname), 사진, 
	private DaangnMemberVO member;			
	
	// ------------------------------------------------------------
	// 추가등등
	// ------------------------------------------------------------
	// 태그
	
	// ------------------------------------------------------------
	
}
