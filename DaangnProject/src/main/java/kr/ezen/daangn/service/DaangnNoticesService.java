package kr.ezen.daangn.service;

import java.util.List;

import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.NoticesVO;
import kr.ezen.daangn.vo.PagingVO;

public interface DaangnNoticesService {
	// 공지사항 페이징 얻기
	PagingVO<NoticesVO> getPagingList(CommonVO cv);
	
	// 공지사항 한개 보기
	NoticesVO getNoticesByIdx(int idx);
	
	// 공지사항 저장하기
	int insertNotices(NoticesVO nv);
	
	// 공지사항 수정하기
	int updateNotices(NoticesVO nv);
	
	// 고정 공지 얻기
	List<NoticesVO> selectByHighlight();
	
	// 공지사항 삭제하기
	int deleteByIdx(int idx);
}
