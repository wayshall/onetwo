--for test, donot line break
@findAll = select t.*, t.rowid from uc_user t order by t.create_time desc
@findAll.count.sql = select count(t.id) from uc_user t 
@find10 = select * from (  select t.*, t.rowid from uc_user t  ) where rownum <= 10

