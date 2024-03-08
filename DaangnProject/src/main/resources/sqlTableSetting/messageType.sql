
CREATE SEQUENCE MessageType_idx_seq;
CREATE TABLE MessageType (
    idx NUMBER PRIMARY KEY,
    typeStr VARCHAR2(255) NOT NULL
);

INSERT INTO MessageType VALUES (MessageType_idx_seq.nextval, 'ENTER');
INSERT INTO MessageType VALUES (MessageType_idx_seq.nextval, 'TALK');
INSERT INTO MessageType VALUES (MessageType_idx_seq.nextval, 'RESERVE');

SELECT * FROM MessageType;