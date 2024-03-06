package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.PopularVO;

@Mapper
public interface PopularDAO {
	// 저장
	void insertPopular(PopularVO p) throws SQLException;
	
	/**
	 * 12시간 사이의 조회, 댓글, 좋아요를 기준으로 점수가 높은 board의 Idx를 리턴하는 메서드
	 * @return 점수가 높은 board의 Idx를 리턴
	 */
	List<Integer> findPopularBoard() throws SQLException;
	
	// 관리자용?
	List<PopularVO> getUserTrendAnalysis(HashMap<String, Integer> map) throws SQLException;
	
	int totalCountPopular(HashMap<String, Integer> map) throws SQLException;
}
