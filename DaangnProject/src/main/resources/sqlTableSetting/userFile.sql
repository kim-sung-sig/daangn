DROP SEQUENCE daangn_user_file_idx_seq;
DROP TABLE daangn_user_file;

CREATE SEQUENCE daangn_user_file_idx_seq;

CREATE TABLE daangn_user_file(
    idx NUMBER PRIMARY KEY,
    ref NUMBER NOT NULL,
    originFileName VARCHAR2(1000) NOT NULL,
    saveFileName VARCHAR2(1000) NOT NULL
);

SELECT * FROM daangn_user_file;
