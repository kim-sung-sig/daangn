package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.DaangnBoardFileVO;

@Mapper
public interface DaangnBoardFileDAO {
	// ref에 해당하는 파일 읽기
	List<DaangnBoardFileVO> selectFileByBoardIdx(int boardIdx) throws SQLException;
	
	void insertFile(DaangnBoardFileVO boardFileVO) throws SQLException;
	
	void deleteLike(int ref) throws SQLException;
}
