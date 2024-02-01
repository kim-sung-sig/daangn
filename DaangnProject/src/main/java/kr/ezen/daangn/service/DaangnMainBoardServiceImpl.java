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

import kr.ezen.daangn.dao.DaangnMainBoardDAO;
import kr.ezen.daangn.suport.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;

@Service(value = "mainBoardService")
@Transactional
public class DaangnMainBoardServiceImpl implements DaangnMainBoardService{

	@Autowired
	DaangnMainBoardDAO mainBoardDAO;
	
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
				JsonNode regionNode = jsonNode.get(region);
				Iterator<String> guNames = regionNode.fieldNames();
				while (guNames.hasNext()) {
					list.add(guNames.next());
				}
			} else { // 리턴 ( 서울시, 강서구) => 화곡1동, 신월동, 목3동  or (서울시, 강서구, 화곡1동) => 화곡1동, 신월동, 목3동
				JsonNode regionNode = jsonNode.get(region);
				JsonNode guNode = regionNode.get(gu);
				Iterator<String> dongNames = guNode.fieldNames();
				while (dongNames.hasNext()) {
					list.add(dongNames.next());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<DaangnMainBoardVO> selectList(CommonVO commonVO) {
		List<DaangnMainBoardVO> list = null;
		HashMap<String, Object> map = new HashMap<>();
		map.put("region", commonVO.getRegion());
		map.put("gu", commonVO.getGu());
		map.put("dong", commonVO.getDong());
		try {
			list = mainBoardDAO.selectList(map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public DaangnMainBoardVO selectByIdx(int idx) {
		DaangnMainBoardVO mainBoardVO = null;
		try {
			mainBoardVO = mainBoardDAO.selectByIdx(idx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mainBoardVO;
	}
}
