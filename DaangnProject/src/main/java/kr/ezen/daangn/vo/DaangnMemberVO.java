package kr.ezen.daangn.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class DaangnMemberVO implements UserDetails{
	private static final long serialVersionUID = 1L;

	private int idx;						// 키필드
	
	private String username;				// id (이메일 형태)
	private String password;				// password
	private String role;					// 권한
	
	private String name;					// 실제 이름 
	private String nickName;				// 닉네임
	
	// sns수신?
	private String email;					// 아이디 비번찾기때 쓸것
	private Integer emailOk;				// 이메일 수신여부
	private Date signUpDate;				// 가입일
	private Date lastLoginDate;				// 로그인한 마지막날짜
	// 메일 수신?
	
	// address
	private String stAddress;				// 도로명주소	(이름 추천)
	private String dtAddress;				// 상세주소		(이름 추천)
	//====================================================================
	
	private DaangnFileVO userFile;			// 프로필 사진!
	private Float userVal;					// 유저평
	
	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
}
