CREATE SEQUENCE daangn_board_idx_seq;

CREATE TABLE daangn_board(
	idx NUMBER PRIMARY KEY ,
	REF NUMBER NOT NULL,
	subject varchar2(100) NOT NULL,
	content varchar2(2000) NOT NULL,
	price NUMBER NOT NULL,
	latitude NUMBER NOT NULL,
	longitude NUMBER NOT NULL,
	location varchar2(200) NOT NULL,
	count NUMBER NOT NULL
)

SELECT * FROM daangn_board;

INSERT INTO DAANGN_BOARD values(daangn_board_idx_seq.nextval,1,'제목','내용',12345,54.000,74.000,'화곡',0);--4번
INSERT INTO DAANGN_BOARD values(daangn_board_idx_seq.nextval,2,'asdf','내용',12345,54.000,74.000,'화곡',0);
INSERT INTO DAANGN_BOARD values(daangn_board_idx_seq.nextval,3,'qwer','내용',12345,54.000,74.000,'화곡',0);
