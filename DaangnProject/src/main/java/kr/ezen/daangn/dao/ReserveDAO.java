package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.ReserveVO;

@Mapper
public interface ReserveDAO {
	void insertReserve(ReserveVO rv) throws SQLException;
	
	void deleteReserveByboardRef(int boardRef) throws SQLException;
	
	List<DaangnMainBoardVO> selectPurchaseListByUserIdx(HashMap<String, Integer> map) throws SQLException;

	int getPurchaseListTotalCountByUserIdx(int userRef) throws SQLException;

	ReserveVO getTBReserveByBoardRef(int boardIdx) throws SQLException;
}
