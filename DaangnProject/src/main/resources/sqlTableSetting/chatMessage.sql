CREATE SEQUENCE chatMessage_idx_seq;

CREATE TABLE chatMessage (
	idx NUMBER PRIMARY KEY,
	chatRoom NUMBER NOT NULL,
	sender NUMBER NOT NULL,
	content varchar2(200) NOT NULL,
	regDate timestamp DEFAULT sysdate,
	readed char(1) DEFAULT 1
)
