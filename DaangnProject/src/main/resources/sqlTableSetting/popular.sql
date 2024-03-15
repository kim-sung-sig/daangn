DROP SEQUENCE tb_Popular_idx_seq;
DROP TABLE tb_popular;
CREATE SEQUENCE tb_Popular_idx_seq;
CREATE TABLE tb_popular(
	idx NUMBER PRIMARY KEY,
	boardRef NUMBER NOT NULL,
	userRef NUMBER NOT NULL,
	interaction NUMBER NOT NULL,
	interaction_time TIMESTAMP DEFAULT sysdate,
	CONSTRAINT fk_tb_popular_board_ref FOREIGN KEY (boardRef) REFERENCES daangn_board(idx) ON DELETE CASCADE,
	CONSTRAINT fk_tb_popular_member_ref FOREIGN KEY (userRef) REFERENCES daangn_member(idx) ON DELETE CASCADE	
);