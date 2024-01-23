package kr.ezen.daangn.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import kr.ezen.daangn.vo.DaangnMemberVO;

public interface DaangnMemberService extends UserDetailsService{
	// 회원가입
	void insert(DaangnMemberVO memberVO);
	// 회원 수정
	void update(DaangnMemberVO memberVO);
	// 회원 권한 수정
	void updateRole(DaangnMemberVO memberVO);
	// 회원 비번 번경
	void updatePassword(DaangnMemberVO memberVO);
	
	// 회원 삭제 idx로
	void deleteByIdx(DaangnMemberVO memberVO);
	// 회원 삭제 username으로
	void deleteByUsername(DaangnMemberVO memberVO);
	
	// selectByUsername
	DaangnMemberVO selectByUsername(String username);
	// selectByIdx
	DaangnMemberVO selectByIdx(int idx);
	
	// selectAll
	List<DaangnMemberVO> selectAll();
	
	// 중복확인
	int selectCountByUsername(String username);
}
