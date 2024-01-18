package kr.ezen.daangn.service;

import kr.ezen.daangn.vo.BoardVO;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.PagingVO;

public interface BoardService {
	// 1. 조회 및 검색 지역검색기능?
	PagingVO<BoardVO> selectList(CommonVO commonVO);
	// 2. 1개 글 보기!
	BoardVO selectByIdx(int idx);
}
