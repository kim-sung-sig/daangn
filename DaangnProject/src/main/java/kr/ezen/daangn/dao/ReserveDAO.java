package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.DaangnMainBoardVO;

@Mapper
public interface ReserveDAO {
	void insertReserve() throws SQLException;
	
	void deleteReserveByIdx(int idx) throws SQLException;
	
	List<DaangnMainBoardVO> selectPurchaseListByUserIdx(HashMap<String, Integer> map) throws SQLException;

	int getPurchaseListTotalCountByUserIdx(int userRef) throws SQLException;
}
