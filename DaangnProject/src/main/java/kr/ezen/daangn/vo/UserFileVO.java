package kr.ezen.daangn.vo;

import lombok.Data;

@Data
public class UserFileVO {
	private int idx;
	private int ref;	// user
	
	private String of;	// 진짜 이름
	private String sf;	// 저장이름
}
