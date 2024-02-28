package kr.ezen.daangn.service;

import java.util.List;

import kr.ezen.daangn.vo.DaangnBoardFileVO;

public interface DaangnBoardFileService {
	// 1. 게시글에 대한 사진 조회하기
	List<DaangnBoardFileVO> selectFileByBoardIdx(int boardIdx);
	// 2. 사진 저장하기!
	void insert(DaangnBoardFileVO fileVO);
	// 3. 게시글이 삭제된경우! db에서 삭제하기!
	void deleteByRef(int boardIdx);
}
