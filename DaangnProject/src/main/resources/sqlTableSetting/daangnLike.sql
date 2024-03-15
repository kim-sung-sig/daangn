DROP TABLE daangn_like_tb;
DROP SEQUENCE daangn_like_tb_idx_seq;

CREATE SEQUENCE daangn_like_tb_idx_seq;

CREATE TABLE daangn_like_tb(
	idx NUMBER PRIMARY KEY,
	userIdx NUMBER NOT NULL,
	boardIdx NUMBER NOT NULL,
	CONSTRAINT fk_daangn_like_tb_board_ref FOREIGN KEY (boardIdx) REFERENCES daangn_board(idx) ON DELETE CASCADE,
	CONSTRAINT fk_daangn_like_tb_member_ref FOREIGN KEY (userIdx) REFERENCES daangn_member(idx) ON DELETE CASCADE
);

SELECT * FROM DAANGN_LIKE_TB dlt ;