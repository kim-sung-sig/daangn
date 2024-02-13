package kr.ezen.daangn.service;

import java.util.List;

import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;

public interface DaangnMainBoardService {
	// 0. 지역리스트 뿌리기
	List<String> regionList(String region, String gu, String dong);

	// 1. 조회 및 검색 지역검색기능?
	List<DaangnMainBoardVO> selectList(CommonVO commonVO);

	// 2. 1개 글 보기!
	DaangnMainBoardVO selectByIdx(int idx);

	// 3. 글쓰기
	int saveMainBoard(DaangnMainBoardVO mainBoardVO);
	// 4. 글삭제하기
	
}
