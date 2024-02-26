package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.DaangnMemberVO;

@Mapper
public interface DaangnMemberDAO {
	// 저장
	void insert(DaangnMemberVO memberVO) throws SQLException;
	
	
	// 로그인
	DaangnMemberVO selectByUsername(String username) throws SQLException;
	
	// idx로 모두 얻기
	DaangnMemberVO selectAllByIdx(int idx) throws SQLException;
	
	// idx로 idx와 nickName만 얻기
	DaangnMemberVO selectByIdx(int idx) throws SQLException;
	
	// 중복확인을 위한 코드
	int selectCountByUsername(String username) throws SQLException;
	
	// 여기부터 관리자 및 마이페이지를 위한 용도
	// 페이징해서 얻기
	List<DaangnMemberVO> selectUser(HashMap<String, Object> map) throws SQLException;
	int selectCountUser(HashMap<String, Object> map) throws SQLException;

	// 수정
	void update(DaangnMemberVO memberVO) throws SQLException;
	// 권한 수정
	void updateRole(DaangnMemberVO memberVO) throws SQLException;
	// 비번 변경
	void updatePassword(DaangnMemberVO memberVO) throws SQLException;
	
	// 삭제 idx
	void deleteByIdx(int idx) throws SQLException;
	// 삭제 username
	void deleteByUsername(String username) throws SQLException;
}
