CREATE SEQUENCE daangn_board_file_idx_seq;

CREATE TABLE daangn_board_file(
	idx NUMBER PRIMARY KEY,
	REF NUMBER NOT NULL,
	originFileName varchar2(1000) NOT NULL,
	saveFileName varchar2(1000) NOT null
);

SELECT * FROM daangn_board_file;
