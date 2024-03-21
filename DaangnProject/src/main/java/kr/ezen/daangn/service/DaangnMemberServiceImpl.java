package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.HashMap;
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
import kr.ezen.daangn.vo.DaangnMemberVO;
import kr.ezen.daangn.vo.PagingVO;
import lombok.extern.slf4j.Slf4j;

@Service(value = "daangnMemberService")
@Slf4j
@Transactional
public class DaangnMemberServiceImpl implements DaangnMemberService{
	// 1. 게시글 지우고 게시글 파일 지우고 popular지우고 코멘트 지우고 chatRoom지우고 chatMessage지우고 유저파일 지우고
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
	public int insert(DaangnMemberVO memberVO) {
		int result = 0;
		if(memberVO != null) {
			try {
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				memberVO.setPassword(passwordEncoder.encode(memberVO.getPassword()));
				memberVO.setRole("ROLE_USER");
				daangnMemberDAO.insert(memberVO);
				result = 1;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/** 유저 정보 수정하기 */
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
	
	/** 마지막으로 로그인한 날짜 업데이트 */
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
	
	/** 유저 삭제하기 */
	@Override
	@Transactional
	public int deleteByIdx(int idx) {
		int result = 0;
		try {
			daangnMemberDAO.deleteByIdx(idx); // cascade 걸어놈
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//=========================================================
	// 유효성검사? 유틸
	//=========================================================
	@Override
	public DaangnMemberVO selectByIdx(int idx) {
		DaangnMemberVO memberVO = null;
		try {
			memberVO = daangnMemberDAO.selectByIdx(idx);
			if(memberVO != null) {
				memberVO.setUserVal(daangnCommentDAO.selectScoreByUserIdx(idx));
				memberVO.setUserFile(daangnUserFileDAO.selectFileByUserIdx(idx));				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberVO;
	}
	
	/** 아이디 중복체크를 위한 메서드 */
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
	
	/** 닉네임 중복체크를 위한 메서드 */
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
	
	/** 비밀번호가 일치하는지 확인하기 위한 메서드 */
	@Override
	public int checkPasswordMatch(DaangnMemberVO sessionUser, String password) {
		int result = 0;
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if(passwordEncoder.matches(password, sessionUser.getPassword())) {
			result = 1;
		}
		return result;
	}
	
	/** 아이디 찾기 => email을 받아 있으면 userName 리턴 */
	@Override
	public String selectUserNameByEmail(String email) {
		String result = null;
		try {
			result = daangnMemberDAO.selectUserNameByEmail(email);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/** username으로 passwordUpdate하기*/
	@Override
	public int updatePasswordByUsername(String username, String password) {
		int result = 0;
		try {
			DaangnMemberVO memberVO = daangnMemberDAO.selectByUsername(username);
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			memberVO.setPassword(passwordEncoder.encode(password));
			DaangnMemberVO sendUserData = new DaangnMemberVO();
			sendUserData.setIdx(memberVO.getIdx());
			sendUserData.setPassword(passwordEncoder.encode(password));
			daangnMemberDAO.update(sendUserData);
			result = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	//=========================================================
	// adminService 나중에 한번에 뭉쳐야겟음..
	//=========================================================
	@Override
	public PagingVO<DaangnMemberVO> getUsers(CommonVO cv) {
		PagingVO<DaangnMemberVO> pv = null;
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("search", cv.getSearch());
			int totalCount = daangnMemberDAO.selectCountUser(map);
			pv = new PagingVO<>(totalCount, cv.getCurrentPage(), cv.getSizeOfPage(), cv.getSizeOfBlock());
			map.put("startNo", pv.getStartNo());
			map.put("endNo", pv.getEndNo());
			List<DaangnMemberVO> list = daangnMemberDAO.selectUser(map);
			pv.setList(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pv;
	}

}
