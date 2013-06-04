@findAll = select t.*, t.rowid from uc_user t order by t.create_time desc
@findAll.count.sql = select count(t.id) from uc_user t 
@findAll.mapped.entity = org.onetwo.common.jfish.UcUserDTO

@find10 = select * from ( 
						select t.*, t.rowid from uc_user t 
					) where rownum <= 10
@find10.mapped.entity = org.onetwo.common.jfish.UcUserDTO


