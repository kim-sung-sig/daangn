DROP TABLE daangn_category ;
DROP SEQUENCE daangn_category_idx_seq;

CREATE SEQUENCE daangn_category_idx_seq;
CREATE TABLE  daangn_category(
	categoryIdx NUMBER PRIMARY KEY,
	categoryName varchar2(100) NOT NULL
);
SELECT * FROM daangn_category;

INSERT INTO daangn_category VALUES (daangn_category_idx_seq.nextval, '디지털/가전');		-- 1 여기서 이름을 일단 미리 만들어 주자
INSERT INTO daangn_category VALUES (daangn_category_idx_seq.nextval, '가구/인테리어');		-- 2 여기서 이름을 일단 미리 만들어 주자
INSERT INTO daangn_category VALUES (daangn_category_idx_seq.nextval, '유아동/유아도서');	-- 3 여기서 이름을 일단 미리 만들어 주자
INSERT INTO daangn_category VALUES (daangn_category_idx_seq.nextval, '생활/가공식품');		-- 4 여기서 이름을 일단 미리 만들어 주자
INSERT INTO daangn_category VALUES (daangn_category_idx_seq.nextval, '스포츠/레저');		-- 5 여기서 이름을 일단 미리 만들어 주자
INSERT INTO daangn_category VALUES (daangn_category_idx_seq.nextval, '여성잡화');			-- 6 여기서 이름을 일단 미리 만들어 주자
INSERT INTO daangn_category VALUES (daangn_category_idx_seq.nextval, '여성의류');			-- 7 여기서 이름을 일단 미리 만들어 주자
INSERT INTO daangn_category VALUES (daangn_category_idx_seq.nextval, '남성패션/잡화');		-- 8 여기서 이름을 일단 미리 만들어 주자
INSERT INTO daangn_category VALUES (daangn_category_idx_seq.nextval, '게임/취미');			-- 9 여기서 이름을 일단 미리 만들어 주자
INSERT INTO daangn_category VALUES (daangn_category_idx_seq.nextval, '뷰티/미용');			-- 10 여기서 이름을 일단 미리 만들어 주자
INSERT INTO daangn_category VALUES (daangn_category_idx_seq.nextval, '반려동물용품');		-- 11 여기서 이름을 일단 미리 만들어 주자
INSERT INTO daangn_category VALUES (daangn_category_idx_seq.nextval, '도서/티켓/음반');	-- 12 여기서 이름을 일단 미리 만들어 주자
INSERT INTO daangn_category VALUES (daangn_category_idx_seq.nextval, '기타'); 				-- 13 여기서 이름을 일단 미리 만들어 주자