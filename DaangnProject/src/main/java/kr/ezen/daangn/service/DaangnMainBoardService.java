package kr.ezen.daangn.service;

import java.util.List;

import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.ScrollVO;

public interface DaangnMainBoardService {
	// 0. 지역리스트 뿌리기
	List<String> regionList(String region, String gu, String dong);
	
	// 스크롤처럼 리스트 리턴
	List<DaangnMainBoardVO> selectScrollList(ScrollVO sv);
	// 1. getLastIdx얻기
	int getLastIdx();
	
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
	
	// 6. 유저가 쓴글 보여주기(userRef, statusRef, lastItemIdx, sizeOfPage)
	List<DaangnMainBoardVO> selectScrollListByUserIdx(ScrollVO sv);
	// 게시글에 해당하는 유저의 다른 게시물목록가져오기 (userRef, boardRef)
	List<DaangnMainBoardVO> selectListByUserIdxAndNotBoardIdx(int userRef, int boardRef);
	// 7. (userRef, statusRef)에 따른 board갯수 얻기
	int getBoardCountByUserIdxAndStatusRef(int userRef, int statusRef);
}
