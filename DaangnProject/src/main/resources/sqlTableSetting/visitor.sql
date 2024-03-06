CREATE SEQUENCE tb_visitor_idx_seq;


--public class VisitVO {
--	private int idx;
--	private String visitIp;			// 접속자 ip
--	private String visitAgent;		// 접속 브라우저
--	private String visitReferer;	// 접속 경로
--	private Date visitTime;			// 방문 시간
--}

CREATE TABLE tb_visitor (
	idx NUMBER PRIMARY KEY,								-- 키필드
	visitIp varchar2(50) NOT null,							-- 접속자 ip
	visitAgent varchar2(200) NOT NULL,						-- 접속 브라우저
	visitReferer varchar2(200) NOT NULL,					-- 접속 경로
	visitTime TIMESTAMP DEFAULT sysdate NOT null			-- 방문 시간
);