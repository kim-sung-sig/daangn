package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ezen.daangn.dao.DaangnBoardFileDAO;
import kr.ezen.daangn.dao.DaangnLikeDAO;
import kr.ezen.daangn.vo.DaangnLikeVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;

@Service(value = "daangnLikeService")
@Transactional
public class DaangnLikeServiceImpl implements DaangnLikeService{
	
	@Autowired
	private DaangnLikeDAO daangnLikeDAO;
	@Autowired
	private DaangnBoardFileDAO daangnBoardFileDAO;
	
	@Override
	public int countLike(int boardIdx) {
		int result = 0;
		try {
			result = daangnLikeDAO.countLike(boardIdx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int insertLike(DaangnLikeVO likeVO) {
		int result = 0;
		try {
			daangnLikeDAO.insertLike(likeVO);
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int deleteLike(DaangnLikeVO likeVO) {
		int result = 0;
		try {
			daangnLikeDAO.deleteLike(likeVO);
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	
	
	
	
	@Override
	public List<DaangnMainBoardVO> selectLikeByUseridx(int userIdx) {
		List<DaangnMainBoardVO> list = null;
		try {
			list = daangnLikeDAO.selectLikeByUseridx(userIdx);
			for(DaangnMainBoardVO boardVO : list) {
				if(boardVO != null) {
					boardVO.setBoardFileList(daangnBoardFileDAO.selectFileByBoardIdx(boardVO.getIdx()));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int select(DaangnLikeVO likeVO) {
		int result = 0;
		try {
			result = daangnLikeDAO.select(likeVO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
