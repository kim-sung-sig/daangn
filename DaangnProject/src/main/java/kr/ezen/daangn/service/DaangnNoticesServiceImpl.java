package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ezen.daangn.dao.DaangnNoticesDAO;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.NoticesVO;
import kr.ezen.daangn.vo.PagingVO;

@Service(value = "daangnNoticesService")
public class DaangnNoticesServiceImpl implements DaangnNoticesService{
	
	@Autowired
	private DaangnNoticesDAO noticesDAO;
	
	@Override
	public PagingVO<NoticesVO> getPagingList(CommonVO cv) {
		PagingVO<NoticesVO> pv = null;
		try {
			HashMap<String, String> map = new HashMap<>();
			int totalCount = noticesDAO.selectCount();
			pv = new PagingVO<>(totalCount, cv.getCurrentPage(), cv.getSizeOfPage(), cv.getSizeOfBlock());
			map.put("startNo", pv.getStartNo() + "");
			map.put("endNo", pv.getEndNo() + "");
			List<NoticesVO> list = noticesDAO.selectList(map);
			pv.setList(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pv;
	}

	@Override
	public NoticesVO getNoticesByIdx(int idx) {
		NoticesVO nv = null;
		try {
			nv = noticesDAO.selectByIdx(idx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nv;
	}

	@Override
	public int insertNotices(NoticesVO nv) {
		int result = 0;
		try {
			noticesDAO.insert(nv);
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int updateNotices(int idx, String title, String content) {
		int result = 0;
		try {
			HashMap<String, String> map = new HashMap<>();
			map.put("idx", idx + "");
			map.put("title", title);
			map.put("content", content);
			noticesDAO.update(map);
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int updateHighLight(int idx, int highlight) {
		int result = 0;
		try {
			HashMap<String, String> map = new HashMap<>();
			map.put("idx", idx + "");
			map.put("highlight", highlight + "");
			noticesDAO.update(map);
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<NoticesVO> selectByHighlight() {
		List<NoticesVO> list = null;
		try {
			list = noticesDAO.selectByHighlight();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int deleteByIdx(int idx) {
		int result = 0;
		try {
			noticesDAO.deleteByIdx(idx);
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
