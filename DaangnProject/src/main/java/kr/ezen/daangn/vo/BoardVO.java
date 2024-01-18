package kr.ezen.daangn.vo;

import java.util.List;

import lombok.Data;

@Data
public class BoardVO {
	private int idx;
	private int ref; // user의 ref인데...
	
	private String subject;		// 제목
	private String content;		// 내용
	private int price;			// 가격
	
	private double latitude;	// 위도
	private double longitude;	// 경도
	private String location;	// 상세위치
	
	private int count;			// 조회수
	
	private MyUserVO myUserVO;
	private List<BoardFileVO> fileList; // 사진등록
	
}
