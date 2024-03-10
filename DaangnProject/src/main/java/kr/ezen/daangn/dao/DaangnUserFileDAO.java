package kr.ezen.daangn.dao;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.DaangnFileVO;

@Mapper
public interface DaangnUserFileDAO {
	/**
	 * 유저 프로필 저장하기
	 * @param boardFileVO
	 * @throws SQLException
	 */
	void insertFile(DaangnFileVO boardFileVO) throws SQLException;
	
	/**
	 * 유저 프로필 삭제하기
	 * @param boardIdx
	 * @throws SQLException
	 */
	void deleteFileByUserIdx(int userIdx) throws SQLException;
	
	/**
	 * 유저 프로필 얻기
	 * @param boardIdx
	 * @return
	 * @throws SQLException
	 */
	DaangnFileVO selectFileByUserIdx(int userIdx) throws SQLException;
	
}
