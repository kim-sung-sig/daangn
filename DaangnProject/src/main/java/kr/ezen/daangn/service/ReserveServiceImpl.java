package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ezen.daangn.dao.DaangnBoardFileDAO;
import kr.ezen.daangn.dao.ReserveDAO;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.PagingVO;
import kr.ezen.daangn.vo.ReserveVO;
import lombok.extern.slf4j.Slf4j;

@Service(value = "reserveService")
@Slf4j
public class ReserveServiceImpl implements ReserveService{
	
	@Autowired
	private ReserveDAO reserveDAO;
	@Autowired
	private DaangnMemberService daangnMemberService;
	@Autowired
	private DaangnBoardFileDAO daangnBoardFileDAO;
	
	
	/** 저장하기 => (삭제후 저장)*/
	@Override
	@Transactional
	public int insertReserve(ReserveVO rv) {
		int result = 0;
		try {
			if(rv != null) {
				if(rv.getIdx() != 0) {
					reserveDAO.deleteReserveByIdx(rv.getIdx());
				}
				reserveDAO.insertReserve();
				result = 1;				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/** 구매목록 얻기 */
	@Override
	public PagingVO<DaangnMainBoardVO> selectPurchaseListByUserIdx(CommonVO cv) {
		PagingVO<DaangnMainBoardVO> pv = null;
		try {
			log.info("selectPurchaseListByUserIdx 실행");
			int totalCount = reserveDAO.getPurchaseListTotalCountByUserIdx(cv.getUserRef());
			log.info("totalCount => {}", totalCount);
			pv = new PagingVO<>(totalCount, cv.getCurrentPage(), cv.getSizeOfPage(), cv.getSizeOfBlock());
			HashMap<String, Integer> map = new HashMap<>();
			map.put("userRef", cv.getUserRef());
			map.put("startNo", pv.getStartNo());
			map.put("endNo", pv.getEndNo());
			List<DaangnMainBoardVO> list = reserveDAO.selectPurchaseListByUserIdx(map);
			log.info("list => {}", list);
			for(DaangnMainBoardVO boardVO : list) {
				boardVO.setMember(daangnMemberService.selectByIdx(boardVO.getUserRef()));				// 유저정보
				boardVO.setBoardFileList(daangnBoardFileDAO.selectFileByBoardIdx(boardVO.getIdx()));	// 파일
			}
			pv.setList(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pv;
	}

	/** boardIdx에 해당하는 Reserve얻기 (예약자가 잇는지 확인하기) */
	@Override
	public ReserveVO getTBReserveByBoardRef(int boardIdx) {
		ReserveVO rv = null;
		try {
			rv = reserveDAO.getTBReserveByBoardRef(boardIdx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rv;
	}
	
}
