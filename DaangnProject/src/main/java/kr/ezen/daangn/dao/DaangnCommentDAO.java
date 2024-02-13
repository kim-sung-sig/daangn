package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.DaangnCommentVO;

@Mapper
public interface DaangnCommentDAO {
	List<DaangnCommentVO> selectCommentByUserIdx(int userIdx) throws SQLException;
	void insertComment(DaangnCommentVO commentVO) throws SQLException;
	void updateComment(DaangnCommentVO commentVO) throws SQLException;
	void deleteCommentByIdx(int idx) throws SQLException;
	void deleteCommentByUserIdx(int userIdx) throws SQLException;
	
	float selectEvalByUserIdx(int userIdx) throws SQLException;
}
