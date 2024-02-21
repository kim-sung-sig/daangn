package kr.ezen.daangn.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class DaangnMainBoardVO {
	private int idx;
	private int ref; // user의 idx
	
	private String subject;		// 제목
	private String content;		// 내용
	private int price;			// 가격
	
	private double latitude;	// 위도 kakaoMap에 표시하기 위한 값들 
	private double longitude;	// 경도
	private String location;	// 상세위치
	private String loc;			// 서울시 강서구 화곡1동 이런값을 가지는 데이터
	
	private int readCount;		// 조회수
	private Date regDate;		// 게시일
	//==================================================
	// db세팅끝
	
	private DaangnMemberVO member;					// 유저 정보담기
	private List<DaangnFileVO> boardFileList;		// 사진등록
	private int countLike;							// 좋아요 수
	private int chatRoomCount;						// 체팅 수 읽기?
}
