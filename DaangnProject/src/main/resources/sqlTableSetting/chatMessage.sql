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
	readed NUMBER DEFAULT 1,
	CONSTRAINT fk_chatMessage_chatRoom_ref FOREIGN KEY (chatRoom) REFERENCES chatRoom(roomIdx) ON DELETE CASCADE,
	CONSTRAINT fk_chatMessage_sender_ref FOREIGN KEY (sender) REFERENCES daangn_member(idx) ON DELETE CASCADE
);

SELECT * FROM CHATMESSAGE c ;
