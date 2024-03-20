package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.NoticesVO;

@Mapper
public interface DaangnNoticesDAO {
	
	List<NoticesVO> selectList(HashMap<String, String> map) throws SQLException;
	
	int selectCount() throws SQLException;
	
	NoticesVO selectByIdx(int idx) throws SQLException;
	
	/** 공지사항 저장하기 */
	void insert(NoticesVO nv) throws SQLException;
	
	/** 공지사항 수정하기 (idx, title, content) or (idx, highlight) */
	void update(HashMap<String, String> map) throws SQLException;
	
	/** 고정 공지 얻기 */
	List<NoticesVO> selectByHighlight() throws SQLException;
	
	void deleteByIdx(int idx) throws SQLException;
}
