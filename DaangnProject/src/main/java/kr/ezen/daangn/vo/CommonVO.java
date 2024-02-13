package kr.ezen.daangn.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 글번호 현제페이지번호 페이지당 글수 페이지목록수를 받기 위한 공통변수들을 가지는 클래스
@Getter
@AllArgsConstructor
public class CommonVO {
	private String region;
	private String gu;
	private String dong;
	private String search="";
}
