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
	
	private String email;
	private String phone;					// 전화 번호 (sms를 하진 않겠지만 카카오로그인을 구현 할 경우 필요)
	
	// address
	private String stAddress;				// 도로명주소	(이름 추천)
	private String dtAddress;				// 상세주소		(이름 추천)
	
	private Date birthDate;					// 생일
	private int gender;						// (db에서 boolean저장불가 임시값 1 남 0 여
	
	
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
