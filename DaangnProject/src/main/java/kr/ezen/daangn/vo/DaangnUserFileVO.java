package kr.ezen.daangn.vo;

import lombok.Data;

@Data
/**
 *  유저의 프로필 자신을 등록하기 위한 vo
 */
public class DaangnUserFileVO {
	private int idx;
	private int ref;	// user
	
	private String originFileName;	// 진짜 이름
	private String saveFileName;	// 저장이름
}
