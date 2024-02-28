package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ezen.daangn.dao.DaangnBoardFileDAO;
import kr.ezen.daangn.vo.DaangnBoardFileVO;

@Service(value = "daangnBoardFileService")
@Transactional
public class DaangnBoardFileServiceImpl implements DaangnBoardFileService{
	
	@Autowired
	private DaangnBoardFileDAO daangnBoardFileDAO;
	
	@Override
	public List<DaangnBoardFileVO> selectFileByBoardIdx(int boardIdx) {
		List<DaangnBoardFileVO> fileList = null;
		try {
			fileList = daangnBoardFileDAO.selectFileByBoardIdx(boardIdx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fileList;
	}

	@Override
	public void insert(DaangnBoardFileVO fileVO) {
		try {
			daangnBoardFileDAO.insertFile(fileVO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteByRef(int boardIdx) {
		try {
			daangnBoardFileDAO.deleteLike(boardIdx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
