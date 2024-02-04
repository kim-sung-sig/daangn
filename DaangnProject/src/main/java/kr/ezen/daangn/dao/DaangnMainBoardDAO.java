package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.DaangnMainBoardVO;

@Mapper
public interface DaangnMainBoardDAO {
	// 리스트 주기
	List<DaangnMainBoardVO> selectList(HashMap<String, Object> map) throws SQLException;
	// 한개 얻기
	DaangnMainBoardVO selectByIdx(int idx) throws SQLException;
	
	// 유저idx 에 해당하는 글 주기
	List<DaangnMainBoardVO> selectByRef(int ref) throws SQLException;
	
	// 글 저장 후 idx 리턴
	int insert(DaangnMainBoardVO mainBoardVO) throws SQLException;
}
