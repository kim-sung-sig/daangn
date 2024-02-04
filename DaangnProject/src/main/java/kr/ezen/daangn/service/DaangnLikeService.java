package kr.ezen.daangn.service;

import java.util.List;

import kr.ezen.daangn.vo.DaangnLikeVO;

public interface DaangnLikeService {
	// 1. 게시글의 좋아요수 알아내기
	int countLike(int boardIdx);
	// 2. 좋아요 하기
	int insertLike(DaangnLikeVO likeVO);
	// 3. 좋아요 취소하기
	int deleteLike(DaangnLikeVO likeVO);
	// 4. 유저가 누른 좋아요한 글 가져오기
	List<Integer> selectLikeByUseridx(int userIdx);
	// 5. 유저가 좋아요를 했었나 확인하기
	int select(DaangnLikeVO likeVO);
}
