package kr.ezen.daangn.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import kr.ezen.daangn.vo.PagingVO;

public interface DaangnMemberService extends UserDetailsService{
	//=========================================================
	// 유저 서비스
	//=========================================================
	// 회원가입
	int insert(DaangnMemberVO memberVO);
	
	// 회원 수정
	int update(DaangnMemberVO memberVO);
	// 마지막 로그인한 날짜 갱신!
	int updateLastLoginDate(int idx);
	
	// 회원 삭제 idx로
	int deleteByIdx(int idx);
	
	//=========================================================
	
	
	//=========================================================
	// 유효성검사? 유틸
	//=========================================================
	// idx로 얻기
	DaangnMemberVO selectByIdx(int idx);
	// 아이디 중복확인
	int selectCountByUsername(String username);
	
	// 닉네임 중복확인
	int selectCountByNickName(String nickName);
	
	// 비밀번호가 일치하는지 확인
	int checkPasswordMatch(DaangnMemberVO sessionUser, String password);
	
	// email로 userName 찾기
	String selectUserNameByEmail(String email);
	//=========================================================
	int updatePasswordByUsername(String username, String password);
	
	//=========================================================
	// adminService 나중에 한번에 뭉쳐야겟음..
	//=========================================================
	// 페이징 getUsers
	PagingVO<DaangnMemberVO> getUsers(CommonVO cv);
}
