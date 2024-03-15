DROP SEQUENCE daangn_board_idx_seq;
DROP TABLE DAANGN_BOARD ;

CREATE SEQUENCE daangn_board_idx_seq;
CREATE TABLE daangn_board(
	idx NUMBER PRIMARY KEY,
	userRef NUMBER NOT NULL,
	categoryRef NUMBER NOT NULL,
	statusRef NUMBER DEFAULT 1,
	subject varchar2(100) NOT NULL,
	content varchar2(2000) NOT NULL,
	price NUMBER NOT NULL,
	latitude NUMBER NOT NULL,
	longitude NUMBER NOT NULL,
	location varchar2(200) NOT NULL,
	loc varchar2(200) NOT NULL,
	readCount NUMBER DEFAULT 0,
	regDate TIMESTAMP DEFAULT SYSDATE,
	CONSTRAINT fk_daangn_board_ref FOREIGN KEY (userRef) REFERENCES daangn_member(idx) ON DELETE CASCADE
);

SELECT * FROM daangn_board;