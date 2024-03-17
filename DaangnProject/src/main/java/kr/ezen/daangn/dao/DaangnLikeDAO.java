package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.DaangnLikeVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;

@Mapper
public interface DaangnLikeDAO {
	void insertLike(DaangnLikeVO likeVO) throws SQLException;
	
	void deleteLike(DaangnLikeVO likeVO) throws SQLException;
	
	int countLike(int boardIdx) throws SQLException;
	
	List<DaangnMainBoardVO> selectLikeByUseridx(int userIdx) throws SQLException;
	
	int select(DaangnLikeVO likeVO) throws SQLException;
	
}
