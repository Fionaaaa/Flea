-- 生成者Oracle SQL Developer Data Modeler 4.1.1.887
--   时间:        2016-03-01 22:27:05 CST
--   站点:      Oracle Database 10g
--   类型:      Oracle Database 10g




CREATE
  TABLE collection
  (
    collect_id    NUMBER NOT NULL ,
    user_id       NUMBER NOT NULL ,
    user_goods_id NUMBER NOT NULL ,
    flag          CHAR (1) NOT NULL
  ) ;
ALTER TABLE collection ADD CONSTRAINT 收藏表_PK PRIMARY KEY ( collect_id ) ;


CREATE
  TABLE goods
  (
    goods_id NUMBER NOT NULL ,
    title NVARCHAR2 (50) NOT NULL ,
    price FLOAT ,
    describe NVARCHAR2 (200) ,
    user_id NUMBER NOT NULL ,
    flag    CHAR (1) NOT NULL ,
    classify NVARCHAR2 (20) NOT NULL
  ) ;
ALTER TABLE goods ADD CONSTRAINT 物品表_PK PRIMARY KEY ( goods_id ) ;


CREATE
  TABLE picture
  (
    picture_id NUMBER NOT NULL ,
    goods_id   NUMBER NOT NULL ,
    location NVARCHAR2 (200) NOT NULL
  ) ;
ALTER TABLE picture ADD CONSTRAINT 图片表_PK PRIMARY KEY ( picture_id ) ;


CREATE
  TABLE "user"
  (
    user_id NUMBER NOT NULL ,
    icon NVARCHAR2 (100) NOT NULL ,
    name NVARCHAR2 (20) NOT NULL ,
    account VARCHAR2 (50) NOT NULL ,
    flag    CHAR (1) NOT NULL ,
    contact NVARCHAR2 (200)
  ) ;
ALTER TABLE "user" ADD CONSTRAINT 用户表_PK PRIMARY KEY ( user_id ) ;


ALTER TABLE picture ADD CONSTRAINT 图片表_物品表_FK FOREIGN KEY ( goods_id )
REFERENCES goods ( goods_id ) ;

ALTER TABLE collection ADD CONSTRAINT 收藏表_用户表_FK FOREIGN KEY ( user_id )
REFERENCES "user" ( user_id ) ;

ALTER TABLE goods ADD CONSTRAINT 物品表_用户表_FK FOREIGN KEY ( user_id ) REFERENCES
"user" ( user_id ) ;


-- Oracle SQL Developer Data Modeler 概要报告: 
-- 
-- CREATE TABLE                             4
-- CREATE INDEX                             0
-- ALTER TABLE                              7
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
