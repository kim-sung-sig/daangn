DROP SEQUENCE chatRoom_idx_seq;
DROP TABLE chatRoom;

CREATE SEQUENCE chatRoom_idx_seq;

CREATE TABLE chatRoom (
	roomIdx NUMBER PRIMARY KEY,
	userIdx NUMBER NOT NULL,
	boardIdx NUMBER NOT NULL,
	FOREIGN KEY (userIdx) REFERENCES daangn_member(idx),
	FOREIGN KEY (boardIdx) REFERENCES daangn_board(idx)
);

SELECT * FROM CHATROOM c ;