package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.DaangnMemberVO;

@Mapper
public interface DaangnMemberDAO {
	// 저장
	void insert(DaangnMemberVO memberVO) throws SQLException;
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
	
	// 로그인
	DaangnMemberVO selectByUsername(String username) throws SQLException;
	// idx로 얻기
	DaangnMemberVO selectByIdx(int idx) throws SQLException;
	
	// 모두 얻기
	List<DaangnMemberVO> selectAll() throws SQLException;
	
	// 중복확인을 위한 코드
	int selectCountByUsername(String username) throws SQLException;
}
