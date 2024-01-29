package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ezen.daangn.dao.DaangnMainBoardDAO;
import kr.ezen.daangn.suport.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;

@Service(value = "mainBoardService")
@Transactional
public class DaangnMainBoardServiceImpl implements DaangnMainBoardService{

	@Autowired
	DaangnMainBoardDAO mainBoardDAO;
	
	@Override
	public List<DaangnMainBoardVO> selectList(CommonVO commonVO) {
		List<DaangnMainBoardVO> list = null;
		HashMap<String, Object> map = new HashMap<>();
		map.put("region", commonVO.getRegion());
		map.put("gu", commonVO.getGu());
		map.put("dong", commonVO.getDong());
		try {
			list = mainBoardDAO.selectList(map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
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
