package kr.ezen.daangn.vo;

import java.util.Date;

import lombok.Data;

@Data
public class NoticesVO {
	private int idx;			// 키필드
	private String title;		// 제목
	private String content;		// 내용 CKEditor 사용예정
	private Date regDate;		// 게시일
	private int highlight;		// 고정(중요)도를 표시하기위한 항목
}
