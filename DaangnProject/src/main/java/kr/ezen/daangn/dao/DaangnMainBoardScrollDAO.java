package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.DaangnMainBoardVO;

@Mapper
public interface DaangnMainBoardScrollDAO {
	
	/**
	 * (lastItemIdx, sizeOfPage, categoryRef, statusRef ,search, region, gu, dong)
	 * 를 Map에 담아 호출하면 lastItemIdx부터 아래로 sizeOfPage갯수만큼 DaangnMainBoardVO를 리턴함
	 */
	List<DaangnMainBoardVO> selectScrollList(HashMap<String, Object> map) throws SQLException;
	
	/** 최대 idx를 구하는 메서드 */
	int getLastIdx() throws SQLException;
}
