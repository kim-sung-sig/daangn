package kr.ezen.daangn.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import kr.ezen.daangn.vo.PagingVO;

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
	// idx로 모두 얻기
	DaangnMemberVO selectAllByIdx(int idx);
	// idx로 idx, nickName만 얻기
	DaangnMemberVO selectByIdx(int idx);
	
	// selectAll
	PagingVO<DaangnMemberVO> getUsers(CommonVO cv);
	
	// 중복확인
	int selectCountByUsername(String username);
	
	
	// 유저가 쓴 글 보여주기!
	List<DaangnMainBoardVO> selectMainBoardByMemberIdx(int idx);
}
