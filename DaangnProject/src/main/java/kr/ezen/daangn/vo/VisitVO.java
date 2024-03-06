package kr.ezen.daangn.vo;

import java.util.Date;

import lombok.Data;

@Data
public class VisitVO {
	private int idx;
	private String visitIp;			// 접속자 ip
	private String visitAgent;		// 접속 브라우저
	private String visitReferer;	// 접속 경로
	private Date visitTime;			// 방문 시간
}
