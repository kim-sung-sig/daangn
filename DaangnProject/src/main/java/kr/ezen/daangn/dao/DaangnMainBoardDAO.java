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
	// 갯수새기
	int selectCount(HashMap<String, Object> map) throws SQLException;
	
	// 한개 얻기
	DaangnMainBoardVO selectByIdx(int idx) throws SQLException;
	
}
