DROP TABLE daangn_like_tb;
DROP SEQUENCE daangn_like_tb_idx_seq;

CREATE SEQUENCE daangn_like_tb_idx_seq;

CREATE TABLE daangn_like_tb(
	idx NUMBER PRIMARY KEY,
	userIdx NUMBER NOT NULL,
	boardIdx NUMBER NOT null
);

SELECT * FROM DAANGN_LIKE_TB dlt ;