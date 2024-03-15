DROP TABLE tb_visitor;
DROP SEQUENCE tb_visitor_idx_seq;
CREATE SEQUENCE tb_visitor_idx_seq;


--public class VisitVO {
--	private int idx;
--	private String visitIp;			// 접속자 ip
--	private Date visitTime;			// 방문 시간
--}

CREATE TABLE tb_visitor (
	idx NUMBER PRIMARY KEY,								-- 키필드
	visitIp varchar2(50) NOT null,							-- 접속자 ip
	visitTime TIMESTAMP DEFAULT sysdate NOT null			-- 방문 시간
);