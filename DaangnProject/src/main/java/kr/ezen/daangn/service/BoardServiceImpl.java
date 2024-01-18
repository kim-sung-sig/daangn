package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ezen.daangn.dao.BoardDAO;
import kr.ezen.daangn.dao.BoardFileDAO;
import kr.ezen.daangn.vo.BoardFileVO;
import kr.ezen.daangn.vo.BoardVO;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.PagingVO;

@Service(value = "boardService")
@Transactional
public class BoardServiceImpl implements BoardService{

	@Autowired
	BoardDAO boardDAO;
	@Autowired
	BoardFileDAO boardFileDAO;
	
	@Override
	/**
	 * @param commonVO에는 currentPage, sizeOfPage, sizeOfBlock 을 가지고 잇는 클래스
	 * @return paging 처리를 한 pagingVO리턴 (list in pagingVO)
	 */
	public PagingVO<BoardVO> selectList(CommonVO commonVO) {
		PagingVO<BoardVO> pv = null;
		try {
			HashMap<String, Object> map = new HashMap<>();
			// 서치 한 것 보내주기!
			map.put("search", commonVO.getSearch());
			int totalCount = boardDAO.selectCount(map);
			pv = new PagingVO<>(totalCount, commonVO.getCurrentPage(), commonVO.getSizeOfPage(), commonVO.getSizeOfBlock()); // 페이지 계산 완료
			map.put("startNo", pv.getStartNo());
			map.put("endNo", pv.getEndNo());
			
			List<BoardVO> boardList = boardDAO.selectList(map);
			// 첨부파일 사진 넣어주기!
			for(BoardVO boardVO: boardList) {
				List<BoardFileVO> boardFileList = boardFileDAO.selectByRef(boardVO.getIdx());
				boardVO.setFileList(boardFileList);
			}
			pv.setList(boardList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pv;
	}

	@Override
	public BoardVO selectByIdx(int idx) {
		BoardVO boardVO = null;
		try {
			boardVO = boardDAO.selectByIdx(idx);
			// 첨부파일 사진 넣어주기!
			boardVO.setFileList(boardFileDAO.selectByRef(boardVO.getIdx()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return boardVO;
	}
	
}
