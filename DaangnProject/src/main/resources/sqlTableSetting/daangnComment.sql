CREATE SEQUENCE daangn_comment_idx_seq;

CREATE TABLE daangn_comment(
	idx NUMBER PRIMARY KEY,
	userIdx NUMBER NOT NULL,
	writerIdx NUMBER NOT NULL,
	score NUMBER NOT NULL,
	content varchar2(200) NOT NULL,
	regDate timestamp DEFAULT sysdate
)