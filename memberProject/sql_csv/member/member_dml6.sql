-- 후보키로 검색
-- 검색구분(종목) : 아이디, 이메일, 휴대폰 (CK(candidate key) : 후보키)
-- 참고) PK(Primary Key:기본키) + AK(Alternate Key:대체키) = CK(Candidate Key:후보키)

-- 회원등급(role) 포함

SELECT DISTINCT m3.*
       , ( SELECT LISTAGG(r2.ROLE, ',')
                    WITHIN GROUP (ORDER BY m2.ID)
             FROM MEMBER_TBL m2, USER_ROLES r2
            WHERE r2.USERNAME = m2.ID
              AND r2.USERNAME = m3.ID ) AS "ROLE"
  FROM MEMBER_TBL m3, USER_ROLES r3
 WHERE m3.ID = r3.USERNAME
   AND ID = 'abcd1111';
   
SELECT DISTINCT m3.*
       , ( SELECT LISTAGG(r2.ROLE, ',')
                    WITHIN GROUP (ORDER BY m2.ID)
             FROM MEMBER_TBL m2, USER_ROLES r2
            WHERE r2.USERNAME = m2.ID
              AND r2.USERNAME = m3.ID ) AS "ROLE"
  FROM MEMBER_TBL m3, USER_ROLES r3
 WHERE m3.ID = r3.USERNAME
   AND EMAIL = 'abcd1111@abcd.com';
   
SELECT DISTINCT m3.*
       , ( SELECT LISTAGG(r2.ROLE, ',')
                    WITHIN GROUP (ORDER BY m2.ID)
             FROM MEMBER_TBL m2, USER_ROLES r2
            WHERE r2.USERNAME = m2.ID
              AND r2.USERNAME = m3.ID ) AS "ROLE"
  FROM MEMBER_TBL m3, USER_ROLES r3
 WHERE m3.ID = r3.USERNAME
   AND MOBILE = '010-1111-3333';