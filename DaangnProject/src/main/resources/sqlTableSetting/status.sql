DROP TABLE daangn_status ;
DROP SEQUENCE daangn_status_idx_seq;
CREATE SEQUENCE daangn_status_idx_seq;



CREATE TABLE  daangn_status(
	statusIdx NUMBER PRIMARY KEY,
	statusName varchar2(100) NOT NULL
);



SELECT * FROM daangn_status;



INSERT INTO daangn_status VALUES (daangn_status_idx_seq.nextval, '판매중'); -- 여기서 이름을 일단 미리 만들어 주자
INSERT INTO daangn_status VALUES (daangn_status_idx_seq.nextval, '예약중'); -- 여기서 이름을 일단 미리 만들어 주자
INSERT INTO daangn_status VALUES (daangn_status_idx_seq.nextval, '판매완료');