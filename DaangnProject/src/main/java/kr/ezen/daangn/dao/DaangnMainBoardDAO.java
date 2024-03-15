package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.DaangnMainBoardVO;

@Mapper
public interface DaangnMainBoardDAO {
	// 한개 얻기
	DaangnMainBoardVO selectByIdx(int idx) throws SQLException;
	
	/** 저장 글 저장 후 idx 리턴 */ 
	void insert(DaangnMainBoardVO mainBoardVO) throws SQLException;
	/** 업데이트 */
	void update(HashMap<String, Object> map) throws SQLException;
	/** 삭제*/
	void deleteByIdx(int idx) throws SQLException;
}
