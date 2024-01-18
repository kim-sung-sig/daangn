package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.BoardFileVO;

@Mapper
public interface BoardFileDAO {
	// ref에 해당하는 파일 읽기
	List<BoardFileVO> selectByRef(int ref) throws SQLException;
	
	void insert(BoardFileVO boardFileVO) throws SQLException;
}
