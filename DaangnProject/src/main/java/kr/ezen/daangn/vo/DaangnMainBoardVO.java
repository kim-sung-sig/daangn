package kr.ezen.daangn.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class DaangnMainBoardVO {
	private int idx;
	private int userRef;		// user의 idx 왜래키
	private int categoryRef;	// category의 idx 왜래키
	private int statusRef;		// status의 idx 왜래키
	
	private String subject;		// 제목
	private String content;		// 내용
	
	private int price;			// 가격 // 중거거래만 가격있음 나머진 0
	
	private double latitude;	// 위도 kakaoMap에 표시하기 위한 값들 
	private double longitude;	// 경도
	private String location;	// 상세위치
	private String loc;			// 서울시 강서구 화곡1동 이런값을 가지는 데이터
	
	private int readCount;		// 조회수
	private Date regDate;		// 게시일
		
	//==================================================
	// db세팅끝
	private List<DaangnBoardFileVO> boardFileList;		// 사진등록
	private DaangnMemberVO member;					// 유저 정보담기
	private String categoryName;					// 카테고리 이름 1~12 중고마켓, 13 생활꿀팁, 14 QnA
	private String statusName;						// 상태 이름	1 판매중, 2 예약중, 3 판매완료
	private int countLike;							// 좋아요 수
	private int chatRoomCount;						// 체팅 수 읽기?
}
