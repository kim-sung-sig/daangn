DROP table notices;
DROP SEQUENCE notices_idx_seq;

CREATE SEQUENCE notices_idx_seq;
CREATE TABLE notices(
	idx NUMBER PRIMARY KEY,
	title varchar2(500) NOT NULL,
	content varchar2(3000) NOT NULL,
	regDate timestamp DEFAULT sysdate,
	private NUMBER DEFAULT 0
);