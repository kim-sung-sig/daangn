DROP SEQUENCE daangn_user_file_idx_seq;
DROP TABLE daangn_user_file;

CREATE SEQUENCE daangn_user_file_idx_seq;

CREATE TABLE daangn_user_file(
    idx NUMBER PRIMARY KEY,
    ref NUMBER NOT NULL,
    originFileName VARCHAR2(1000) NOT NULL,
    saveFileName VARCHAR2(1000) NOT NULL,
    CONSTRAINT fk_daangn_user_file_ref FOREIGN KEY (ref) REFERENCES daangn_member(idx) ON DELETE CASCADE
);

SELECT * FROM daangn_user_file;
