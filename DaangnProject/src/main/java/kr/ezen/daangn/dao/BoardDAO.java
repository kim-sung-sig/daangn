package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.BoardVO;

@Mapper
public interface BoardDAO {
	// 리스트 주기
	List<BoardVO> selectList(HashMap<String, Object> map) throws SQLException;
	// 갯수새기
	int selectCount(HashMap<String, Object> map) throws SQLException;
	
	
	// 한개 얻기
	BoardVO selectByIdx(int idx) throws SQLException;
	
}
