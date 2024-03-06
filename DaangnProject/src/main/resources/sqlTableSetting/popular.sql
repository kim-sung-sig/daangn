CREATE SEQUENCE tb_Popular_idx_seq;
CREATE TABLE tb_popular(
	idx NUMBER PRIMARY KEY,
	boardRef NUMBER NOT NULL,
	userRef NUMBER NOT NULL,
	interaction NUMBER NOT NULL,
	interaction_time TIMESTAMP DEFAULT sysdate,
	FOREIGN KEY (boardRef) REFERENCES daangn_board(idx),
	FOREIGN KEY (userRef) REFERENCES daangn_member(idx)
);