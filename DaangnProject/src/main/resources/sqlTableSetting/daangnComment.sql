DROP TABLE daangn_comment;
DROP SEQUENCE daangn_comment_idx_seq;
CREATE SEQUENCE daangn_comment_idx_seq;

CREATE TABLE daangn_comment(
	idx NUMBER PRIMARY KEY,
	userIdx NUMBER NOT NULL,
	writerIdx NUMBER NOT NULL,
	score NUMBER NOT NULL,
	content varchar2(200) NOT NULL,
	regDate timestamp DEFAULT sysdate,
	CONSTRAINT fk_daangn_comment_userIdx_ref FOREIGN KEY (userIdx) REFERENCES daangn_member(idx) ON DELETE CASCADE,
	CONSTRAINT fk_daangn_comment_wrIdx_ref FOREIGN KEY (writerIdx) REFERENCES daangn_member(idx) ON DELETE CASCADE
)

SELECT * FROM daangn_comment;