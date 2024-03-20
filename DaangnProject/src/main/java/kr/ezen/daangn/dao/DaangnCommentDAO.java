package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.DaangnCommentVO;

@Mapper
public interface DaangnCommentDAO {
	// 평점계산
	float selectScoreByUserIdx(int userIdx) throws SQLException;
	// 저장
	void insertComment(DaangnCommentVO commentVO) throws SQLException;
	// 댓글얻기
	List<DaangnCommentVO> selectCommentByUserIdx(int userIdx) throws SQLException;
	// 댓글지우기
	void deleteCommentByIdx(int idx) throws SQLException;
	
	void updateComment(DaangnCommentVO commentVO) throws SQLException;

}
