DROP SEQUENCE daangn_board_file_idx_seq;
DROP TABLE daangn_board_file;

CREATE SEQUENCE daangn_board_file_idx_seq;

CREATE TABLE daangn_board_file(
    idx NUMBER PRIMARY KEY,
    boardRef NUMBER NOT NULL,
    originFileName VARCHAR2(1000) NOT NULL,
    saveFileName VARCHAR2(1000) NOT NULL,
    FOREIGN KEY (boardRef) REFERENCES daangn_board(idx)
);

SELECT * FROM daangn_board_file;
