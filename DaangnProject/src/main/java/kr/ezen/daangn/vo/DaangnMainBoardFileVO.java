package kr.ezen.daangn.vo;

import lombok.Data;

@Data
public class DaangnMainBoardFileVO {
	private int idx;
	private int ref;	// board
	
	private String originFileName;	// 진짜 이름
	private String saveFileName;	// 저장이름
}
