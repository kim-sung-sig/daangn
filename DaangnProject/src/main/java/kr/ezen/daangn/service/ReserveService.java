package kr.ezen.daangn.service;

import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.PagingVO;
import kr.ezen.daangn.vo.ReserveVO;

/** 이게 판매중인지 예약중인지 판매완료ㅕ인지 체크하는 */
public interface ReserveService {
	// 예약테이블 저장하기
	int insertReserve(ReserveVO rv);
	
	PagingVO<DaangnMainBoardVO> selectPurchaseListByUserIdx(CommonVO cv);

	ReserveVO getTBReserveByBoardRef(int boardIdx);
}
