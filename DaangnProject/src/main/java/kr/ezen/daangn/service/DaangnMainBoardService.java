package kr.ezen.daangn.service;

import kr.ezen.daangn.suport.CommonVO;
import kr.ezen.daangn.suport.PagingVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;

public interface DaangnMainBoardService {
	// 1. 조회 및 검색 지역검색기능?
	PagingVO<DaangnMainBoardVO> selectList(CommonVO commonVO);
	// 2. 1개 글 보기!
	DaangnMainBoardVO selectByIdx(int idx);
}
