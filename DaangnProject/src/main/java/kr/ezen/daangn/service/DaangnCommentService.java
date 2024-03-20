package kr.ezen.daangn.service;

import kr.ezen.daangn.vo.DaangnCommentVO;

public interface DaangnCommentService {
	// 유저평 쓰기
	int insert(DaangnCommentVO commentVO);
	
	// 유저평 삭제
	int deleteCommentByIdx(int idx);
	
	// 유저평 수정
	int updateComment(DaangnCommentVO commentVO);
}
