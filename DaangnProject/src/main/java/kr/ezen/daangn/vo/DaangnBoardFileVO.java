package kr.ezen.daangn.vo;

import lombok.Data;

@Data
public class DaangnBoardFileVO {
	private int idx;
	private int boardRef;			// board or user
	private String originFileName;	// 진짜 이름
	private String saveFileName;	// 저장이름
}
