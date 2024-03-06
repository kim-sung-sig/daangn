package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import kr.ezen.daangn.dao.VisitDAO;
import kr.ezen.daangn.vo.CommonVO;
import kr.ezen.daangn.vo.PagingVO;
import kr.ezen.daangn.vo.VisitVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "visitService")
public class VisitService implements HttpSessionListener{
	
	private static int activeSessionCount;

	@Autowired
	private VisitDAO visitDAO;
	
	/**
	 * 현재 접속자수를 반환하는 메서드
	 * @return
	 */
	public static int getActiveSessionCount() {
		return activeSessionCount;
	}
	/**
	 * 세션이 생성될때 실행되는 메서드
	 * 여기서 방문자 **자동 저장 처리**
	 */
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		activeSessionCount++;
		try {
			HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	        // 세션에서 HttpServletRequest를 직접 얻습니다.
	        String ipAddress = req.getRemoteAddr();
	        String userAgent = req.getHeader("User-Agent");
	        String referer = req.getHeader("referer"); // 접속 전 사이트 정보
	        
	        VisitVO visitor = new VisitVO();
	        visitor.setVisitIp(ipAddress);
	        visitor.setVisitAgent(userAgent);
	        visitor.setVisitReferer(referer);
	        log.info("방문자가 접속하였습니다. 방문자 정보: {}", visitor);
			visitDAO.saveVisitor(visitor);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 해당날의 방문자수 얻기
	 */
	public int getDailyVisitorCount(String strDate){
		int result = 0;
		try {
			result = visitDAO.getVisitorCountByDate(strDate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 방문자 총합 얻기
	 */
	public int getTotalVisitorCount(){
		int result = 0;
		try {
			result = visitDAO.getVisitorCountByDate(null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
		
	/**
	 * 관리자용 방문자 정보보기
	 */
	public PagingVO<VisitVO> getVisitors(CommonVO cv){
		PagingVO<VisitVO> pv = null;
		cv.setS(50);
		cv.setB(5);
		try {
			HashMap<String, Integer> map = new HashMap<>();
			int totalCount = visitDAO.getVisitorCountByDate(null);
			pv = new PagingVO<>(totalCount, cv.getCurrentPage(), cv.getSizeOfPage(), cv.getSizeOfBlock());
			map.put("startNo", pv.getStartNo());
			map.put("endNo", pv.getEndNo());
			List<VisitVO> list = visitDAO.getPagedVisitor(map);
			pv.setList(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pv;
	}
}
