DROP TABLE daangn_member;
DROP SEQUENCE daangn_member_idx_seq;
CREATE SEQUENCE daangn_member_idx_seq;

CREATE TABLE daangn_member(
	idx NUMBER PRIMARY KEY,
	username varchar2(100) NOT NULL,
	password varchar2(100) NOT NULL,
	role varchar2(10) NOT NULL,
	name varchar2(100) NOT NULL,
	nickName varchar2(100) NOT NULL, -- 중복체크
	email varchar2(100) NOT NULL,
	emailOk char(1) DEFAULT 0, -- 1이 수신 거부
	signUpDate timestamp DEFAULT sysdate,
	lastLoginDate timestamp DEFAULT sysdate,
	stAddress varchar2(100) NOT NULL,
	dtAddress varchar2(100) NOT NULL
);

SELECT * FROM daangn_member;

INSERT INTO daangn_member VALUES (daangn_member_idx_seq.nextval,'admin','123456','ROLE_ADMIN','admin','admin',' ',' ',1,sysdate,sysdate,' ',' ');
INSERT INTO daangn_member VALUES (daangn_member_idx_seq.nextval,'master','123456','ROLE_ADMIN','master','master',' ',' ',1,sysdate,sysdate,' ',' ');
INSERT INTO daangn_member VALUES (daangn_member_idx_seq.nextval,'webmaster','123456','ROLE_ADMIN','webmaster','webmaster',' ',' ',1,sysdate,sysdate,' ',' ');
INSERT INTO daangn_member VALUES (daangn_member_idx_seq.nextval,'root','123456','ROLE_ADMIN','root','root',' ',' ',1,sysdate,sysdate,' ',' ');
INSERT INTO daangn_member VALUES (daangn_member_idx_seq.nextval,'dba','123456','ROLE_ADMIN','dba','dba',' ',' ',1,sysdate,sysdate,' ',' ');
