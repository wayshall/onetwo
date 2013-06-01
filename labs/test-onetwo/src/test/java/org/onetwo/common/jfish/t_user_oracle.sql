-- Create table
create table T_USER
(
  ID               number,
  USER_NAME        varchar2(50),
  STATUS           varchar2(20),
  EMAIL            varchar2(50),
  BIRTH_DAY        date,
  HEIGHT           number,
  CREATE_TIME      date,
  LAST_UPDATE_TIME date
)
;
-- Create/Recreate primary, unique and foreign key constraints 
alter table T_USER
  add constraint user_id_primary primary key (ID);

  
create sequence SEQ_T_USER start with 100 increment by 1 maxvalue 999999999999999999999999999