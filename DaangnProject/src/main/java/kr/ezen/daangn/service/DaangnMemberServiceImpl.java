package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ezen.daangn.dao.DaangnMainBoardDAO;
import kr.ezen.daangn.dao.DaangnMemberDAO;
import kr.ezen.daangn.vo.DaangnMainBoardVO;
import kr.ezen.daangn.vo.DaangnMemberVO;
import lombok.extern.slf4j.Slf4j;

@Service(value = "daangnMemberService")
@Slf4j
@Transactional
public class DaangnMemberServiceImpl implements DaangnMemberService{
	
	@Autowired
	private DaangnMemberDAO daangnMemberDAO;
	
	@Autowired
	private DaangnMainBoardDAO daangnMainBoardDAO;
	
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

	@Override
	public void insert(DaangnMemberVO memberVO) {
		if(memberVO != null) {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			memberVO.setPassword(passwordEncoder.encode(memberVO.getPassword()));
			memberVO.setRole("ROLE_USER");
			try {
				daangnMemberDAO.insert(memberVO);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update(DaangnMemberVO memberVO) {
		if(memberVO != null) {
			try {
				DaangnMemberVO dbMemberVO = daangnMemberDAO.selectAllByIdx(memberVO.getIdx());
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				if(passwordEncoder.matches(memberVO.getPassword(), dbMemberVO.getPassword())) {
					daangnMemberDAO.update(memberVO);					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void updateRole(DaangnMemberVO memberVO) {
		if(memberVO != null) {
			try {
				DaangnMemberVO dbMemberVO = daangnMemberDAO.selectAllByIdx(memberVO.getIdx());
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				if(passwordEncoder.matches(memberVO.getPassword(), dbMemberVO.getPassword())) {
					daangnMemberDAO.updateRole(memberVO);					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void updatePassword(DaangnMemberVO memberVO) {
		if(memberVO != null) {
			try {
				DaangnMemberVO dbMemberVO = daangnMemberDAO.selectAllByIdx(memberVO.getIdx());
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				if(passwordEncoder.matches(memberVO.getPassword(), dbMemberVO.getPassword())) {
					daangnMemberDAO.updatePassword(memberVO);					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deleteByIdx(DaangnMemberVO memberVO) {
		if(memberVO != null) {
			try {
				DaangnMemberVO dbMemberVO = daangnMemberDAO.selectAllByIdx(memberVO.getIdx());
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				if(passwordEncoder.matches(memberVO.getPassword(), dbMemberVO.getPassword())) {
					daangnMemberDAO.deleteByIdx(memberVO.getIdx());					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deleteByUsername(DaangnMemberVO memberVO) {
		if(memberVO != null) {
			try {
				DaangnMemberVO dbMemberVO = daangnMemberDAO.selectAllByIdx(memberVO.getIdx());
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				if(passwordEncoder.matches(memberVO.getPassword(), dbMemberVO.getPassword())) {
					daangnMemberDAO.deleteByUsername(memberVO.getUsername());					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public DaangnMemberVO selectByUsername(String username) {
		DaangnMemberVO memberVO = null;
		try {
			memberVO = daangnMemberDAO.selectByUsername(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberVO;
	}

	@Override
	public DaangnMemberVO selectAllByIdx(int idx) {
		DaangnMemberVO memberVO = null;
		try {
			memberVO = daangnMemberDAO.selectAllByIdx(idx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberVO;
	}
	@Override
	public DaangnMemberVO selectByIdx(int idx) {
		DaangnMemberVO memberVO = null;
		try {
			memberVO = daangnMemberDAO.selectByIdx(idx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberVO;
	}

	@Override
	public List<DaangnMemberVO> selectAll() {
		List<DaangnMemberVO> list = null;
		try {
			list = daangnMemberDAO.selectAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int selectCountByUsername(String username) {
		int count = 0;
		try {
			count = daangnMemberDAO.selectCountByUsername(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public List<DaangnMainBoardVO> selectMainBoardByMemberIdx(int idx) {
		List<DaangnMainBoardVO> list = null;
		try {
			list = daangnMainBoardDAO.selectByRef(idx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
