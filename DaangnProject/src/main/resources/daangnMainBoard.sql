DROP SEQUENCE daangn_board_idx_seq;
DROP TABLE DAANGN_BOARD ;

CREATE SEQUENCE daangn_board_idx_seq;
CREATE TABLE daangn_board(
	idx NUMBER PRIMARY KEY ,
	REF NUMBER NOT NULL,
	subject varchar2(100) NOT NULL,
	content varchar2(2000) NOT NULL,
	price NUMBER NOT NULL,
	latitude NUMBER NOT NULL,
	longitude NUMBER NOT NULL,
	location varchar2(200) NOT NULL,
	loc varchar2(200) NOT NULL,
	readCount NUMBER DEFAULT 0,
	regDate TIMESTAMP DEFAULT SYSDATE
);

SELECT * FROM daangn_board;
INSERT INTO DAANGN_BOARD VALUES (daangn_board_idx_seq.nextval,6,'세탁기팔아요','내용',35000,37.54024495118844,126.83891917835108,'화곡역 1번출구','서울특별시 강서구 화곡1동',0, SYSDATE);
INSERT INTO DAANGN_BOARD VALUES (daangn_board_idx_seq.nextval,2,'닌텐도 팔아요','내용',5200,37.54024495118844,126.83891917835108,'화곡역 1번출구','서울특별시 강서구 화곡1동',0, SYSDATE);
INSERT INTO DAANGN_BOARD VALUES (daangn_board_idx_seq.nextval,3,'그림팝니다','내용',68000,37.54024495118844,126.83891917835108,'화곡역 1번출구','서울특별시 강서구 화곡1동',0, SYSDATE);