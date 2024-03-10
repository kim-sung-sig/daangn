package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ezen.daangn.dao.DaangnCommentDAO;
import kr.ezen.daangn.dao.DaangnMemberDAO;
import kr.ezen.daangn.dao.DaangnUserFileDAO;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import kr.ezen.daangn.vo.PagingVO;
import lombok.extern.slf4j.Slf4j;

@Service(value = "daangnMemberService")
@Slf4j
@Transactional
public class DaangnMemberServiceImpl implements DaangnMemberService{
	
	@Autowired
	private DaangnMemberDAO daangnMemberDAO;
	@Autowired
	private DaangnCommentDAO daangnCommentDAO;
	@Autowired
	private DaangnUserFileDAO daangnUserFileDAO;
		
	//=========================================================
	// 유저 서비스
	//=========================================================
	@Override
	public DaangnMemberVO loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info(" : " + username + "으로 호출");
		DaangnMemberVO memberVO = null;
		try {
			memberVO = daangnMemberDAO.selectByUsername(username);
			if(memberVO == null) {
				throw new UsernameNotFoundException("지정 아이디가 없습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		log.info(memberVO + "리턴");
		return memberVO;
	}
	
	/**
	 * 저장하기
	 * 유저 비빌번호를 암호화한 후 저장
	 */
	@Override
	public void insert(DaangnMemberVO memberVO) {
		if(memberVO != null) {
			try {
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				memberVO.setPassword(passwordEncoder.encode(memberVO.getPassword()));
				memberVO.setRole("ROLE_USER");
				daangnMemberDAO.insert(memberVO);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 유저 정보 수정하기
	 */
	@Override
	public int update(DaangnMemberVO memberVO) {
		int result = 0;
		try {
			if(memberVO.getPassword() != null) { // 비번이 넘어왓다면 비번 암호화 하고
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				memberVO.setPassword(passwordEncoder.encode(memberVO.getPassword()));
			}
			daangnMemberDAO.update(memberVO);
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 마지막으로 로그인한 날짜 업데이트
	 */
	@Override
	public int updateLastLoginDate(int idx) {
		int result = 0;
		try {
			daangnMemberDAO.updateLastLoginDate(idx);
			result = 1;
		} catch (SQLException e) {
			log.error("Failed to update lastLoginDate for userIdx:" + idx, e);
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 유저 삭제하기
	 */
	@Override
	public int deleteByIdx(int idx) {
		int result = 0;
		try {
			daangnMemberDAO.deleteByIdx(idx);
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public List<DaangnMainBoardVO> selectMainBoardByMemberIdx(int idx) {
		return null;
	}
	
	
	//=========================================================
	// 유효성검사? 유틸
	//=========================================================
	@Override
	public DaangnMemberVO selectByIdx(int idx) {
		DaangnMemberVO memberVO = null;
		try {
			memberVO = daangnMemberDAO.selectByIdx(idx);
			memberVO.setUserVal(daangnCommentDAO.selectScoreByUserIdx(idx));
			memberVO.setUserFile(daangnUserFileDAO.selectFileByUserIdx(idx));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberVO;
	}
	
	/**
	 * 아이디 중복체크를 위한 메서드
	 */
	@Override
	public int selectCountByUsername(String username) {
		int result = 0;
		try {
			result = daangnMemberDAO.selectCountByUsername(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 닉네임 중복체크를 위한 메서드 (아직 미사용)
	 */
	@Override
	public int selectCountByNickName(String nickName) {
		int result = 0;
		try {
			result = daangnMemberDAO.selectCountByNickName(nickName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 비밀번호가 일치하는지 확인하기 위한 메서드
	 */
	@Override
	public int checkPasswordMatch(DaangnMemberVO sessionUser, String password) {
		int result = 0;
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if(passwordEncoder.matches(password, sessionUser.getPassword())) {
			result = 1;
		}
		return result;
	}
	
	//=========================================================
	// adminService 나중에 한번에 뭉쳐야겟음..
	//=========================================================
	@Override
	public PagingVO<DaangnMemberVO> getUsers(CommonVO cv) {
		return null;
	}

}
