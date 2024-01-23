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
	email varchar2(100) NOT NULL,
	phone varchar2(100) NOT NULL,
	stAddress varchar2(100) NOT NULL,
	dtAddress varchar2(100) NOT NULL,
	birthDate timestamp NOT NULL,
	gender char(1) DEFAULT 1
);

SELECT * FROM daangn_member;

INSERT INTO daangn_member VALUES (daangn_member_idx_seq.nextval,'admin','123456','ROLE_ADMIN','admin','admin',' ',' ',' ',' ', sysdate, 1);
INSERT INTO daangn_member VALUES (daangn_member_idx_seq.nextval,'master','123456','ROLE_ADMIN','master','master',' ',' ',' ',' ', sysdate, 1);
INSERT INTO daangn_member VALUES (daangn_member_idx_seq.nextval,'webmaster','123456','ROLE_ADMIN','webmaster','webmaster',' ',' ',' ',' ', sysdate, 1);
INSERT INTO daangn_member VALUES (daangn_member_idx_seq.nextval,'root','123456','ROLE_ADMIN','root','root',' ',' ',' ',' ', sysdate, 1);
INSERT INTO daangn_member VALUES (daangn_member_idx_seq.nextval,'dba','123456','ROLE_ADMIN','dba','dba',' ',' ',' ',' ', sysdate, 1);
