package kr.ezen.daangn.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.ezen.daangn.dao.DaangnBoardFileDAO;
import kr.ezen.daangn.dao.DaangnMainBoardDAO;
import kr.ezen.daangn.dao.DaangnMainBoardScrollDAO;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.PagingVO;
import kr.ezen.daangn.vo.ScrollVO;
import lombok.extern.slf4j.Slf4j;

@Service(value = "daangnMainBoardService")
@Transactional
@Slf4j
public class DaangnMainBoardServiceImpl implements DaangnMainBoardService{

	@Autowired
	private DaangnMainBoardDAO daangnMainBoardDAO;
	@Autowired
	private DaangnMainBoardScrollDAO daangnMainBoardScrollDAO;
	@Autowired
	private DaangnMemberService daangnMemberService;
	@Autowired
	private DaangnBoardFileDAO daangnBoardFileDAO;
	
	// 0.
	@Override
	public List<String> regionList(String region , String gu , String dong ) {
		List<String> list = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode jsonNode = objectMapper.readTree(new File("src/main/resources/static/data/data.json"));
			// 리턴() => 서울시, 강원도,.. 
			if(region == null) {
				Iterator<String> regionNames = jsonNode.fieldNames();
				while (regionNames.hasNext()) {
					list.add(regionNames.next());
				}
			} else if (gu == null){ // 리턴 (서울시)=>강서구, 양천구 ...
				JsonNode regionNode = jsonNode.path(region);
				Iterator<String> guNames = regionNode.fieldNames();
				while (guNames.hasNext()) {
					list.add(guNames.next());
				}
			} else { // 리턴 ( 서울시, 강서구) => 화곡1동, 신월동, 목3동  or (서울시, 강서구, 화곡1동) => 화곡1동, 신월동, 목3동
				JsonNode guNode = jsonNode.path(region).path(gu);
				log.info("-".repeat(120));
				if (guNode.isArray()) {
					for (JsonNode dongNode : guNode) {
						list.add(dongNode.asText());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 목록보여주기 페이징처리
	 * @param CommonVO
	 * @return List<DaangnMainBoardVO>
	 */
	@Override
	public PagingVO<DaangnMainBoardVO> selectList(CommonVO cv) {
		PagingVO<DaangnMainBoardVO> pv = null;
		try {
			HashMap<String, String> map = new HashMap<>();
			map.put("region", cv.getRegion());
			map.put("gu", cv.getGu());
			map.put("dong", cv.getDong());
			map.put("search", cv.getSearch());
			map.put("categoryNum", cv.getCategoryNum() + "");
			map.put("statusNum", cv.getStatusNum() + "");
			log.info("selectList 실행 : map : {}", map);
			int totalCount = daangnMainBoardDAO.selectCount(map);
			pv = new PagingVO<>(totalCount, cv.getCurrentPage(), cv.getSizeOfPage(), cv.getSizeOfBlock());
			
			map.put("startNo", pv.getStartNo() + "");
			map.put("endNo", pv.getEndNo() + "");
			List<DaangnMainBoardVO> list = daangnMainBoardDAO.selectList(map);
			for(DaangnMainBoardVO boardVO : list) {
				boardVO.setMember(daangnMemberService.selectByIdx(boardVO.getUserRef()));				// 유저정보
				boardVO.setBoardFileList(daangnBoardFileDAO.selectFileByBoardIdx(boardVO.getIdx()));	// 파일
			}
			pv.setList(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pv;
	}

	/** idx로 한개 얻기 */
	@Override
	public DaangnMainBoardVO selectByIdx(int idx) {
		DaangnMainBoardVO boardVO = null;
		try {
			boardVO = daangnMainBoardDAO.selectByIdx(idx);
			if(boardVO != null) {
				boardVO.setMember(daangnMemberService.selectByIdx(boardVO.getUserRef()));				// 유저정보			
				boardVO.setBoardFileList(daangnBoardFileDAO.selectFileByBoardIdx(boardVO.getIdx()));	// 파일
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return boardVO;
	}
	
	
	/** 저장하기 */
	@Override
	public int saveMainBoard(DaangnMainBoardVO board) {
		int result = 0;
		try {
			daangnMainBoardDAO.insert(board);
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/** 조회수 증가 */
	@Override
	public void updateReadCount(int idx) {
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("idx", idx);
			map.put("readCount", "1");
			daangnMainBoardDAO.update(map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** 상태변경 (idx, statusRef)*/
	@Override
	public void updateStatus(DaangnMainBoardVO board) {
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("idx", board.getIdx());
			map.put("statusRef", board.getStatusRef());
			daangnMainBoardDAO.update(map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** 글 수정하기 */
	@Override
	public int update(DaangnMainBoardVO board) {
		int result = 0;
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("idx", board.getIdx());
			map.put("categoryRef", board.getCategoryRef());
			map.put("subject", board.getSubject());
			map.put("content", board.getContent());
			map.put("price", board.getPrice());
			map.put("latitude", board.getLatitude());
			map.put("longitude", board.getLongitude());
			map.put("location", board.getLocation());
			map.put("loc", board.getLoc());
			daangnMainBoardDAO.update(map);
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/** 글 삭제하기 */
	@Override
	public int deleteByIdx(int idx) {
		int result = 0;
		try {
			daangnMainBoardDAO.deleteByIdx(idx);
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	/** 
	 * lastItemIdx와 sizeOfPage 및 나머지 카테고리정보를 받아 리스트 뿌리기
	 * @param ScrollVO sv
	 * */
	@Override
	public List<DaangnMainBoardVO> selectScrollList(ScrollVO sv) {
		List<DaangnMainBoardVO> list = null;
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("lastItemIdx", sv.getLastItemIdx());
			map.put("sizeOfPage", sv.getSizeOfPage());
			map.put("categoryRef", sv.getCategoryRef());
			map.put("search", sv.getSearch());
			map.put("region", sv.getRegion());
			map.put("gu", sv.getGu());
			map.put("dong", sv.getDong());
			if(sv.getUserRef() != 0) {
				map.put("userRef", sv.getUserRef());
			}
			if(sv.getStatusRef() != 0) {
				map.put("statusRef", sv.getStatusRef());
			}
			list = daangnMainBoardScrollDAO.selectScrollList(map);
			for(DaangnMainBoardVO boardVO : list) {
				if(boardVO != null) {
					boardVO.setBoardFileList(daangnBoardFileDAO.selectFileByBoardIdx(boardVO.getIdx()));
					boardVO.setMember(daangnMemberService.selectByIdx(boardVO.getUserRef()));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public int getLastIdx() {
		int result = 0;
		try {
			result = daangnMainBoardScrollDAO.getLastIdx();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/** 유저가 쓴 글 주기! (userRef, statusRef, lastItemIdx, sizeOfPage) */
	@Override
	public List<DaangnMainBoardVO> selectScrollListByUserIdx(ScrollVO sv) {
		List<DaangnMainBoardVO> list = null;
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("lastItemIdx", sv.getLastItemIdx());
			map.put("sizeOfPage", sv.getSizeOfPage());
			map.put("userRef", sv.getUserRef());
			map.put("statusRef", sv.getStatusRef());
			list = daangnMainBoardScrollDAO.selectScrollList(map);
			for(DaangnMainBoardVO boardVO : list) {
				if(boardVO != null) {
					boardVO.setBoardFileList(daangnBoardFileDAO.selectFileByBoardIdx(boardVO.getIdx()));
					boardVO.setMember(daangnMemberService.selectByIdx(boardVO.getUserRef()));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/** board에 해당하는 유저가 쓴 글 주기! (userRef, boardRef) */
	@Override
	public List<DaangnMainBoardVO> selectListByUserIdxAndNotBoardIdx(int userRef, int boardRef) {
		List<DaangnMainBoardVO> list = null;
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("userRef", userRef);
			map.put("boardRef", boardRef);
			map.put("lastItemIdx", daangnMainBoardScrollDAO.getLastIdx() + 1); // 최신순
			map.put("sizeOfPage", 2); // 2개만
			map.put("statusRef", 1); // 판매중이 물품만
			list = daangnMainBoardScrollDAO.selectScrollList(map);
			for(DaangnMainBoardVO boardVO : list) {
				if(boardVO != null) {
					boardVO.setBoardFileList(daangnBoardFileDAO.selectFileByBoardIdx(boardVO.getIdx()));
					boardVO.setMember(daangnMemberService.selectByIdx(boardVO.getUserRef()));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/** (userRef, statusRef)에 따른 board갯수 얻기 */
	@Override
	public int getBoardCountByUserIdxAndStatusRef(int userRef, int statusRef) {
		int result = 0;
		try {
			HashMap<String, Integer> map = new HashMap<>();
			map.put("userRef", userRef);
			map.put("statusRef", statusRef);
			result = daangnMainBoardScrollDAO.getBoardCountByUserIdxAndStatusRef(map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
