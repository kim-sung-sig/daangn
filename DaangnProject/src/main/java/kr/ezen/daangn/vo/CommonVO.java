package kr.ezen.daangn.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 글번호 현제페이지번호 페이지당 글수 페이지목록수를 받기 위한 공통변수들을 가지는 클래스
@Getter @ToString
@NoArgsConstructor
public class CommonVO {
	public CommonVO(String region, String gu, String dong, String search) {
		super();
		this.region = region;
		this.gu = gu;
		this.dong = dong;
		this.search = search;
	}
	// 지역 검색
	private String region;
	private String gu;
	private String dong;
	// 단어 검색
	private String search;
	// 카테고리검색
	private Integer categoryNum;
	// 상태체크 (판매, 예약, 판매완료)
	private Integer statusNum;
	//
	private Integer userRef;
	
	
	// 페이징
	private int p = 1;
	private int s = 18;
	private int b = 5;
	
	private int currentPage=1;
	private int sizeOfPage=18;
	private int sizeOfBlock=5;
	
	public void setP(int p) {
		this.p = p;
		setCurrentPage(p);
	}
	public void setS(int s) {
		this.s = s;
		setSizeOfPage(s);
	}
	public void setB(int b) {
		this.b = b;
		setSizeOfBlock(b);
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public void setSizeOfPage(int sizeOfPage) {
		this.sizeOfPage = sizeOfPage;
	}
	public void setSizeOfBlock(int sizeOfBlock) {
		this.sizeOfBlock = sizeOfBlock;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public void setGu(String gu) {
		this.gu = gu;
	}
	public void setDong(String dong) {
		this.dong = dong;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public void setCategoryNum(Integer categoryNum) {
		this.categoryNum = categoryNum;
	}
	public void setStatusNum(Integer statusNum) {
		this.statusNum = statusNum;
	}
}
