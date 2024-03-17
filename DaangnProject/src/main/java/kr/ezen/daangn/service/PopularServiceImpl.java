package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ezen.daangn.dao.DaangnBoardFileDAO;
import kr.ezen.daangn.dao.PopularDAO;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.PagingVO;
import kr.ezen.daangn.vo.PopularVO;
import lombok.extern.slf4j.Slf4j;

@Service(value = "popularService")
@Slf4j
public class PopularServiceImpl implements PopularService{
	
	@Autowired
	private PopularDAO popularDAO;
	@Autowired
	private DaangnMainBoardService daangnMainBoardService;
	@Autowired
	private DaangnMemberService daangnMemberService;
	@Autowired
	private DaangnBoardFileDAO daangnBoardFileDAO;
	
	/**
	 * 저장하기
	 * interaction => 1:조회, 2:댓글, 3:좋아요, 4:좋아요 취소
	 */
	@Override
	public void insertPopular(PopularVO p) {
		try {
			popularDAO.insertPopular(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 관리자용 페이징보기
	 */
	@Override
	public PagingVO<PopularVO> getUserTrendAnalysis(CommonVO cv) {
		PagingVO<PopularVO> pv = null; // jungboardVo에 유저 및 board 정보가 들어있어 이걸로함
		try {
			// 1. popular의 크기를 구한다.
			HashMap<String, Integer> map = new HashMap<>();
			map.put("userRef", cv.getUserRef());
			int totalCount = popularDAO.totalCountPopular(map);
			pv = new PagingVO<>(totalCount, cv.getCurrentPage(), cv.getSizeOfPage(), cv.getSizeOfBlock());
			map.put("startNo", pv.getStartNo());
			map.put("endNo", pv.getEndNo());
			List<PopularVO> popularList = popularDAO.getUserTrendAnalysis(map);
			for(PopularVO p : popularList) {
				p.setMember(daangnMemberService.selectByIdx(p.getUserRef()));
				p.setBoard(daangnMainBoardService.selectByIdx(p.getBoardRef()));
			}
			pv.setList(popularList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pv;
	}
	
	/** 인기게시물 얻기 (나중에 스크롤 페이징 예정.....) */
	@Override
	public List<DaangnMainBoardVO> findPopularBoard() {
		List<DaangnMainBoardVO> list = null;
		try {
			List<Integer> popularList = popularDAO.findPopularBoard();
			list = new ArrayList<>();
			for(Integer boardRef : popularList) {
				DaangnMainBoardVO board = daangnMainBoardService.selectByIdx(boardRef);
				if(board != null) {
					list.add(board);					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/** 유저의 최근 방문 목록 페이징 처리 
	 * @param int userRef, int currentPage
	 * */
	@Override
	public PagingVO<DaangnMainBoardVO> getRecentVisitsBoardByUserIdx(CommonVO cv) {
		PagingVO<DaangnMainBoardVO> pv = null;
		try {
			cv.setS(5);
			cv.setB(5);
			log.info("getRecentVisitsBoardByUserIdx 실행 userRef = {}, currentPage = {}", cv);
			int totalCount = popularDAO.getRecentVisitsBoardTotalCountByUserIdx(cv.getUserRef());
			pv = new PagingVO<>(totalCount, cv.getCurrentPage(), cv.getSizeOfPage(), cv.getSizeOfBlock());
			HashMap<String, Integer> map = new HashMap<>();
			map.put("userRef", cv.getUserRef());
			map.put("startNo", pv.getStartNo());
			map.put("endNo", pv.getEndNo());
			List<DaangnMainBoardVO> list = popularDAO.getRecentVisitsBoardByUserIdx(map);
			for(DaangnMainBoardVO boardVO : list) {
				boardVO.setMember(daangnMemberService.selectByIdx(boardVO.getUserRef()));				// 유저정보
				boardVO.setBoardFileList(daangnBoardFileDAO.selectFileByBoardIdx(boardVO.getIdx()));	// 파일
			}
			pv.setList(list);
			log.info("pv : {}", pv);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pv;
	}
}
