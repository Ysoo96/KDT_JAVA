CREATE TABLE CU_TBL ( 
	ID NUMBER(19,0),
	FOOD_NAME NVARCHAR2(255),
	FOOD_IMG NVARCHAR2(255), 
	FOOD_TYPE1 NVARCHAR2(255),
	FOOD_TYPE2 NVARCHAR2(255), 
	FOOD_DETAIL NVARCHAR2(1000),
	FOOD_PRICE NUMBER(10)
);

ALTER TABLE CU_TBL ADD PRIMARY KEY (ID);
ALTER TABLE CU_TBL MODIFY (FOOD_NAME NOT NULL ENABLE);
  
CREATE SEQUENCE CU_SEQ
MINVALUE 1 
MAXVALUE 9999999999 
INCREMENT BY 1
START WITH 1
NOCYCLE;