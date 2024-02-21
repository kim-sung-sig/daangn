package kr.ezen.daangn.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 글번호 현제페이지번호 페이지당 글수 페이지목록수를 받기 위한 공통변수들을 가지는 클래스
@Getter
@NoArgsConstructor
public class CommonVO {
	public CommonVO(String region, String gu, String dong, String search) {
		super();
		this.region = region;
		this.gu = gu;
		this.dong = dong;
		this.search = search;
	}
	private String region;
	private String gu;
	private String dong;
	private String search;
	
	private int p = 1;
	private int s = 10;
	private int b = 10;
	
	private int currentPage=1;
	private int sizeOfPage=20;
	private int sizeOfBlock=5;
	
	public void setP(int p) {
		this.p = p;
		setCurrentPage(p);
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
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
}
