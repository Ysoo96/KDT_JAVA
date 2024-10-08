-- 별도 계정 생성(테스트용)
CREATE USER spring IDENTIFIED BY spring;

GRANT CONNECT, RESOURCE TO spring;


CREATE  TABLE users (
  username VARCHAR(45) NOT NULL primary key,
  password VARCHAR(60) NOT NULL,
  enabled number(1) DEFAULT 1
 );

CREATE TABLE user_roles (
  user_role_id number(11) NOT NULL,
  username varchar(45) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  CONSTRAINT fk_username FOREIGN KEY (username) 
     REFERENCES users (username)
);
 
CREATE SEQUENCE user_roles_seq
	start with 1
	increment by 1
	maxvalue 99999
    nocycle; 

 -- remember-me 항목 사용시 정보 저장 (선택 사항)
CREATE TABLE persistent_logins (
   username varchar(64) not null, 
   series varchar(64) primary key, 
   token varchar(64) not null, 
   last_used timestamp not null
);  