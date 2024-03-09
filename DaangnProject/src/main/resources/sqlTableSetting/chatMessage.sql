DROP TABLE chatMessage;
DROP SEQUENCE chatMessage_idx_seq;
CREATE SEQUENCE chatMessage_idx_seq;

CREATE TABLE chatMessage (
	idx NUMBER PRIMARY KEY,
	chatRoom NUMBER NOT NULL,
	typeRef NUMBER NOT NULL,
	sender NUMBER NOT NULL,
	content varchar2(200) NOT NULL,
	regDate timestamp DEFAULT sysdate,
	readed NUMBER DEFAULT 1
);

SELECT * FROM CHATMESSAGE c ;
