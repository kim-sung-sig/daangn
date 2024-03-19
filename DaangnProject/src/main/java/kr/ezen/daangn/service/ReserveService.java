package kr.ezen.daangn.service;

import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.PagingVO;
import kr.ezen.daangn.vo.ReserveVO;

/** 이게 판매중인지 예약중인지 판매완료ㅕ인지 체크하는 */
public interface ReserveService {
	// 예약테이블 저장하기
	int insertReserve(ReserveVO rv);
	// 예약테이블 삭제하기
	int deleteReserveByBoardIdx(int boardIdx);
	// 구매목록얻기
	PagingVO<DaangnMainBoardVO> selectPurchaseListByUserIdx(CommonVO cv);
	// 예약자가 있는지 확인하기
	ReserveVO getTBReserveByBoardRef(int boardIdx);
}
