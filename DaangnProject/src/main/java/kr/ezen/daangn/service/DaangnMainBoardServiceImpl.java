package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ezen.daangn.dao.DaangnMainBoardDAO;
import kr.ezen.daangn.suport.CommonVO;
import kr.ezen.daangn.suport.PagingVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;

@Service(value = "daangnMainBoardServiceImpl")
@Transactional
public class DaangnMainBoardServiceImpl implements DaangnMainBoardService{

	@Autowired
	DaangnMainBoardDAO mainBoardDAO;
	
	@Override
	public PagingVO<DaangnMainBoardVO> selectList(CommonVO commonVO) {
		PagingVO<DaangnMainBoardVO> pv = null;
		try {
			HashMap<String, Object> map = new HashMap<>();
			// 서치 한 것 보내주기!
			map.put("search", commonVO.getSearch());
			int totalCount = mainBoardDAO.selectCount(map);
			pv = new PagingVO<>(totalCount, commonVO.getCurrentPage(), commonVO.getSizeOfPage(), commonVO.getSizeOfBlock()); // 페이지 계산 완료
			map.put("startNo", pv.getStartNo());
			map.put("endNo", pv.getEndNo());
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pv;
	}

	@Override
	public DaangnMainBoardVO selectByIdx(int idx) {
		DaangnMainBoardVO mainBoardVO = null;
		try {
			mainBoardVO = mainBoardDAO.selectByIdx(idx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mainBoardVO;
	}
	
}
