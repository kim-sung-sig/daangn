package kr.ezen.daangn.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ezen.daangn.dao.DaangnUserFileDAO;
import kr.ezen.daangn.vo.DaangnFileVO;

@Service(value = "daangnUserFileService")
@Transactional
public class DaangnUserFileServiceImpl implements DaangnUserFileService{
	
	@Autowired
	private DaangnUserFileDAO daangnUserFileDAO;
	
	@Override
	public void insert(DaangnFileVO fileVO) {
		try {
			daangnUserFileDAO.insertFile(fileVO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteByUserIdx(int userIdx) {
		try {
			daangnUserFileDAO.deleteFileByUserIdx(userIdx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
