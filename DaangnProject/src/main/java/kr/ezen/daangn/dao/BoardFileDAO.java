package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.DaangnMainBoardFileVO;

@Mapper
public interface BoardFileDAO {
	// ref에 해당하는 파일 읽기
	List<DaangnMainBoardFileVO> selectByRef(int ref) throws SQLException;
	
	void insert(DaangnMainBoardFileVO mainBoardFileVO) throws SQLException;
}
