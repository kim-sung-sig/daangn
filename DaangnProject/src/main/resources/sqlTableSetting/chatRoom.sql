DROP SEQUENCE chatRoom_idx_seq;
DROP TABLE chatRoom;

CREATE SEQUENCE chatRoom_idx_seq;

CREATE TABLE chatRoom (
	roomIdx NUMBER PRIMARY KEY,
	userIdx NUMBER NOT NULL,
	boardIdx NUMBER NOT NULL,
	boardUserIdx NUMBER NOT NULL,
	lastUpdateDate TIMESTAMP DEFAULT SYSDATE,
	CONSTRAINT fk_chatRoom_userIdx_ref FOREIGN KEY (userIdx) REFERENCES daangn_member(idx) ON DELETE CASCADE,
	CONSTRAINT fk_chatRoom_boardIdx_ref FOREIGN KEY (boardIdx) REFERENCES daangn_board(idx) ON DELETE CASCADE,
	CONSTRAINT fk_chatRoom_boardUserIdx_ref FOREIGN KEY (boardUserIdx) REFERENCES daangn_member(idx) ON DELETE CASCADE
);

SELECT * FROM CHATROOM c ;