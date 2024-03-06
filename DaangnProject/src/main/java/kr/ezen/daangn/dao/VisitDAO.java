package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.VisitVO;

@Mapper
public interface VisitDAO {
	
	// 저장
	void saveVisitor(VisitVO visitor) throws SQLException;
	
	// 하루방문자 얻기 => date가 널이면 총합 방문자수
	int getVisitorCountByDate(String strDate) throws SQLException;
	
	// 관리자용 페이징
	List<VisitVO> getPagedVisitor(HashMap<String, Integer> map) throws SQLException;
	
}
