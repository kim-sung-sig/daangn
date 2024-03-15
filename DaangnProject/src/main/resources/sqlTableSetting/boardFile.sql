DROP SEQUENCE daangn_board_file_idx_seq;
DROP TABLE daangn_board_file;

CREATE SEQUENCE daangn_board_file_idx_seq;

CREATE TABLE daangn_board_file (
    idx NUMBER PRIMARY KEY,
    ref NUMBER NOT NULL,
    originFileName VARCHAR2(1000) NOT NULL,
    saveFileName VARCHAR2(1000) NOT NULL,
    CONSTRAINT fk_daangn_board_file_ref FOREIGN KEY (ref) REFERENCES daangn_board(idx) ON DELETE CASCADE
);

SELECT * FROM daangn_board_file;
