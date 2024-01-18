package kr.ezen.daangn.vo;

import java.util.List;

import lombok.Data;

@Data
public class MyUserVO {
	private int idx;
	private String username;	// id
	private String password;	// password
	
	private String name;
	private String email;
	private String phone;
	
	// 추가 등등
	private int sum;
	private int count;
	
	private UserFileVO userFileVO; // 프로필 사진용
	
	private List<String> userComment; // 유저한줄 코맨트?
}
