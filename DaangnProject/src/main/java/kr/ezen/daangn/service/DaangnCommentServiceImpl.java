package kr.ezen.daangn.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ezen.daangn.dao.DaangnCommentDAO;
import kr.ezen.daangn.vo.DaangnCommentVO;

@Service(value = "daangnCommentService")
public class DaangnCommentServiceImpl implements DaangnCommentService{
	
	@Autowired
	private DaangnCommentDAO daangnCommentDAO;
	
	@Override
	public int insert(DaangnCommentVO commentVO) {
		int result = 0;
		try {
			daangnCommentDAO.insertComment(commentVO);
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int deleteCommentByIdx(int idx) {
		int result = 0;
		try {
			daangnCommentDAO.deleteCommentByIdx(idx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int updateComment(DaangnCommentVO commentVO) {
		int result = 1;
		try {
			daangnCommentDAO.updateComment(commentVO);
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
