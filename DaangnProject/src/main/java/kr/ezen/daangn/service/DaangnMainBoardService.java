package kr.ezen.daangn.service;

import java.util.List;

import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.PagingVO;

public interface DaangnMainBoardService {
	// 0. 지역리스트 뿌리기
	List<String> regionList(String region, String gu, String dong);

	// 1. 조회 및 검색 지역검색기능?
	PagingVO<DaangnMainBoardVO> selectList(CommonVO commonVO);
	
	// 2. 1개 글 보기!
	DaangnMainBoardVO selectByIdx(int idx);

	// 3. 글쓰기
	int saveMainBoard(DaangnMainBoardVO board);
	
	// 4. 조회수 증가시키기
	void updateReadCount(int idx);
	
	// 5. status 변경하기
	int update(DaangnMainBoardVO board);
	
	// 6. 게시글 삭제하기
	int deleteByIdx(int idx);
	
	// 6. 유저가 쓴글 보여주기
	List<DaangnMainBoardVO> selectByUserIdx(int userIdx);
}
