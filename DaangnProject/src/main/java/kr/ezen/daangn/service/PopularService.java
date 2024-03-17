package kr.ezen.daangn.service;

import java.util.List;

import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.PagingVO;
import kr.ezen.daangn.vo.PopularVO;

public interface PopularService {
	// 1. 저장하기
	void insertPopular(PopularVO p);
	// 2. 관리자용 분석용
	PagingVO<PopularVO> getUserTrendAnalysis(CommonVO cv);
	
	// 인기게시물 가져오기 (20개 한정임)
	List<DaangnMainBoardVO> findPopularBoard();
	
	
	// 최근 방문 목록 가져오기!
	PagingVO<DaangnMainBoardVO> getRecentVisitsBoardByUserIdx(CommonVO cv);
}
