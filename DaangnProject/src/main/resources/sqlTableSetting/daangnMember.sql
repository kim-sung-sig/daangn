DROP TABLE daangn_member;
DROP SEQUENCE daangn_member_idx_seq;
CREATE SEQUENCE daangn_member_idx_seq;

CREATE TABLE daangn_member(
	idx NUMBER PRIMARY KEY,
	username varchar2(100) NOT NULL,
	password varchar2(100) NOT NULL,
	role varchar2(10) NOT NULL,
	name varchar2(100) NOT NULL,
	nickName varchar2(100) NOT NULL,
	-- 닉네임 중복체크
	email varchar2(100) NOT NULL,
	-- 이메일 수신확인
	-- 가입일
	-- 마지막으로 로그인한 날
	stAddress varchar2(100) NOT NULL,
	dtAddress varchar2(100) NOT NULL
);

SELECT * FROM daangn_member;

INSERT INTO daangn_member VALUES (daangn_member_idx_seq.nextval,'admin','123456','ROLE_ADMIN','admin','admin',' ',' ',' ');
INSERT INTO daangn_member VALUES (daangn_member_idx_seq.nextval,'master','123456','ROLE_ADMIN','master','master',' ',' ',' ');
INSERT INTO daangn_member VALUES (daangn_member_idx_seq.nextval,'webmaster','123456','ROLE_ADMIN','webmaster','webmaster',' ',' ',' ');
INSERT INTO daangn_member VALUES (daangn_member_idx_seq.nextval,'root','123456','ROLE_ADMIN','root','root',' ',' ',' ');
INSERT INTO daangn_member VALUES (daangn_member_idx_seq.nextval,'dba','123456','ROLE_ADMIN','dba','dba',' ',' ',' ');
