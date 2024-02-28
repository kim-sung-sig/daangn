DROP TABLE daangn_like_tb;
DROP SEQUENCE daangn_like_tb_idx_seq;

CREATE SEQUENCE daangn_like_tb_idx_seq;

CREATE TABLE daangn_like_tb(
	idx NUMBER PRIMARY KEY,
	userIdx NUMBER NOT NULL,
	boardIdx NUMBER NOT NULL,
	FOREIGN KEY (userIdx) REFERENCES daangn_member(idx),
	FOREIGN KEY (boardIdx) REFERENCES daangn_board(idx)
);

SELECT * FROM DAANGN_LIKE_TB dlt ;