@UcUserDao.find.all = select t.*, t.rowid from uc_user t order by t.create_time desc
@UcUserDao.find.all.count.sql = select count(t.id) from uc_user t 
@UcUserDao.find.all.mapped.entity = org.onetwo.common.jfish.UcUserDTO

@UcUserDao.find.10 = select * from ( 
						select t.*, t.rowid from uc_user t 
					) where rownum <= 10
@UcUserDao.find.10.mapped.entity = org.onetwo.common.jfish.UcUserDTO


