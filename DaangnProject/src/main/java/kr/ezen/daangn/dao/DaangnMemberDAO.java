package kr.ezen.daangn.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ezen.daangn.vo.DaangnMemberVO;

@Mapper
public interface DaangnMemberDAO {
	/** 저장하기 */
	void insert(DaangnMemberVO memberVO) throws SQLException;
	/** 로그인 */
	DaangnMemberVO selectByUsername(String username) throws SQLException;
	/** idx에 해당하는 DaangnMemberVO 리턴*/
	DaangnMemberVO selectByIdx(int idx) throws SQLException;
	/** 중복확인을 위한 코드 */
	int selectCountByUsername(String username) throws SQLException;
	/** 중복확인을 위한 코드 */
	int selectCountByNickName(String nickName) throws SQLException;
	/** email에 해당하는 username 리턴 */
	String selectUserNameByEmail(String email) throws SQLException;
	
	
	// 마이페이지 및 관리자 페이지용
	/** 페이징해서 얻기 */
	List<DaangnMemberVO> selectUser(HashMap<String, Object> map) throws SQLException;
	/** 전체개수 얻기 */
	int selectCountUser(HashMap<String, Object> map) throws SQLException;
	/** 수정하기 */
	void update(DaangnMemberVO memberVO) throws SQLException;
	/** 마지막으로 로그인 할 날짜 업데이트 */
	void updateLastLoginDate(int idx) throws SQLException;
	/** 삭제 idx */
	void deleteByIdx(int idx) throws SQLException;
}
