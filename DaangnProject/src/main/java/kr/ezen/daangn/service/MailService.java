package kr.ezen.daangn.service;

import java.util.List;
import java.util.Map;

import kr.ezen.daangn.vo.DaangnMemberVO;


public interface MailService {
	public String mailSend(String to);
	public Map<String, List<DaangnMemberVO>> adminMailSend(List<Integer> userIdxList, String title, String subject);
}
