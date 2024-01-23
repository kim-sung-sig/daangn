package kr.ezen.daangn.suport;

import lombok.Getter;
import lombok.ToString;

// 글번호 현제페이지번호 페이지당 글수 페이지목록수를 받기 위한 공통변수들을 가지는 클래스
@Getter @ToString
public class CommonVO {
	private int p = 1;
	private int s = 10;
	private int b = 10;
	private int idx = 0;
	private int currentPage=1;
	private int sizeOfPage=10;
	private int sizeOfBlock=10;
	
	private String search="";
	
	private String mode = "insert";
	
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
	public void setIdx(int idx) {
		this.idx = idx;
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
	public void setMode(String mode) {
		this.mode = mode;
	}
	public void setSearch(String search) {
		this.search = search;
	}
}
