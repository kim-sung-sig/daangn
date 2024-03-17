package kr.ezen.daangn.vo;

import lombok.Data;

@Data
public class ScrollVO {
	private int lastItemIdx;
	private int sizeOfPage;
	private Integer categoryRef;
	private String search;
	private String region;
	private String gu;
	private String dong;
	private int chatRoomIdx;
	private int userRef;
	private int statusRef;
	private int boardRef;
}
