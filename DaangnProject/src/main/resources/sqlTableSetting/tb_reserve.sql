DROP TABLE tb_reserve;
DROP SEQUENCE tb_reserve_idx_seq;

CREATE SEQUENCE tb_reserve_idx_seq;
CREATE TABLE tb_reserve (
    idx NUMBER PRIMARY KEY,
    boardRef NUMBER NOT NULL,
    userRef NUMBER NOT NULL,
    interaction NUMBER NOT NULL,
    CONSTRAINT fk_boardRef FOREIGN KEY (boardRef) REFERENCES daangn_board(idx) ON DELETE CASCADE,
    CONSTRAINT fk_userRef FOREIGN KEY (userRef) REFERENCES daangn_member(idx) ON DELETE CASCADE
);

SELECT * FROM TB_RESERVE tr ;