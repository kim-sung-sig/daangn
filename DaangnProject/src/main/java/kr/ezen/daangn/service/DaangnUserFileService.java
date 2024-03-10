package kr.ezen.daangn.service;

import kr.ezen.daangn.vo.DaangnFileVO;

public interface DaangnUserFileService {
	void insert(DaangnFileVO fileVO);
	void deleteByUserIdx(int userIdx);
}
