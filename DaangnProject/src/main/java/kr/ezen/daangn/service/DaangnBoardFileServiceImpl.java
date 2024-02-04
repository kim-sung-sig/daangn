package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ezen.daangn.dao.DaangnBoardFileDAO;
import kr.ezen.daangn.vo.DaangnFileVO;

@Service(value = "daangnBoardFileService")
@Transactional
public class DaangnBoardFileServiceImpl implements DaangnBoardFileService{
	
	@Autowired
	private DaangnBoardFileDAO daangnBoardFileDAO;
	
	@Override
	public List<DaangnFileVO> selectFileByBoardIdx(int boardIdx) {
		List<DaangnFileVO> fileList = null;
		return fileList;
	}

	@Override
	public void insert(DaangnFileVO fileVO) {
		try {
			daangnBoardFileDAO.insertFile(fileVO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteByRef(int boardIdx) {
		
	}

}
