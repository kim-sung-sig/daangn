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
import kr.ezen.daangn.dao.DaangnChatRoomDAO;
import kr.ezen.daangn.dao.DaangnCommentDAO;
import kr.ezen.daangn.dao.DaangnLikeDAO;
import kr.ezen.daangn.dao.DaangnMainBoardDAO;
import kr.ezen.daangn.dao.DaangnMemberDAO;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import lombok.extern.slf4j.Slf4j;

@Service(value = "daangnMainBoardService")
@Transactional
@Slf4j
public class DaangnMainBoardServiceImpl implements DaangnMainBoardService{

	@Autowired
	private DaangnMainBoardDAO daangnMainBoardDAO;
	@Autowired
	private DaangnMemberDAO daangnMemberDAO;
	@Autowired
	private DaangnBoardFileDAO daangnBoardFileDAO;
	@Autowired
	private DaangnCommentDAO daangnCommentDAO;
	@Autowired
	private DaangnLikeDAO daangnLikeDAO;
	@Autowired
	private DaangnChatRoomDAO daangnChatRoomDAO;
	
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
	public List<DaangnMainBoardVO> selectList(CommonVO commonVO) {
		List<DaangnMainBoardVO> list = null;
		try {
			list = daangnMainBoardDAO.selectList(commonVO);
			for(DaangnMainBoardVO boardVO : list) {
				boardVO.setMember(daangnMemberDAO.selectByIdx(boardVO.getRef()));						// 유저정보
				boardVO.setCountLike(daangnLikeDAO.countLike(boardVO.getIdx()));						// 좋아요수
				boardVO.setBoardFileList(daangnBoardFileDAO.selectFileByBoardIdx(boardVO.getIdx()));	// 파일
				boardVO.setChatRoomCount(daangnChatRoomDAO.selectCountByBoardIdx(boardVO.getIdx()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 한개 보기
	 * @param int idx
	 * @return DaangnMainBoardVO
	 */
	@Override
	public DaangnMainBoardVO selectByIdx(int idx) {
		DaangnMainBoardVO boardVO = null;
		try {
			boardVO = daangnMainBoardDAO.selectByIdx(idx);
			boardVO.setMember(daangnMemberDAO.selectByIdx(boardVO.getRef()));						// 유저정보
			boardVO.setCountLike(daangnLikeDAO.countLike(boardVO.getIdx()));						// 좋아요수
			boardVO.setBoardFileList(daangnBoardFileDAO.selectFileByBoardIdx(boardVO.getIdx()));	// 파일
			boardVO.setChatRoomCount(daangnChatRoomDAO.selectCountByBoardIdx(boardVO.getIdx()));	// 채팅 수
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return boardVO;
	}
	
	
	/**
	 * 저장하기
	 * @param DaangnMainBoardVO
	 */
	@Override
	public int saveMainBoard(DaangnMainBoardVO boardVO) {
		int result = 0;
		try {
			result = daangnMainBoardDAO.insert(boardVO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 유저가 쓴 글 주기!
	 * @param int userIdx
	 * @return List<DaangnMainBoardVO>
	 */
	@Override
	public List<DaangnMainBoardVO> selectByUserIdx(int userIdx) {
		List<DaangnMainBoardVO> list = null;
		try {
			list = daangnMainBoardDAO.selectByRef(userIdx);
			for(DaangnMainBoardVO boardVO : list) {
				boardVO.setMember(daangnMemberDAO.selectByIdx(boardVO.getRef()));						// 유저정보
				boardVO.setCountLike(daangnLikeDAO.countLike(boardVO.getIdx()));						// 좋아요수
				boardVO.setBoardFileList(daangnBoardFileDAO.selectFileByBoardIdx(boardVO.getIdx()));	// 파일
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
