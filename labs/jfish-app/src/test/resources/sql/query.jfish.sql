@BmRegionDTO.find.all = select t.* from BM_REGION t 
@BmRegionDTO.find.all.count.sql = select count(t.id) from BM_REGION t 
@BmRegionDTO.find.all.mapped.entity = org.onetwo.common.jfish.BmRegionDTO
@BmRegionDTO.find.all.ignore.null = true

@BmRegionDTO.find.10 = select * from ( 
						select t.* from BM_REGION t 
					) where rownum <= 10
@BmRegionDTO.find.10.mapped.entity = org.onetwo.common.jfish.BmRegionDTO

@BmRegionDTO.find.page = select t.* from uc_user t
@BmRegionDTO.find.page.mapped.entity = org.onetwo.common.jfish.BmRegionDTO
@BmRegionDTO.find.page.count.sql = select count(t.id) from uc_user t 

@BmRegionDTO.with.null.cause.page = select t.* from BM_REGION t where t.parentcode= :parentcode and t.grade= :grade
@BmRegionDTO.with.null.cause.page.mapped.entity = org.onetwo.common.jfish.BmRegionDTO

@BmRegionDTO.with.ignore.null.page = select t.* from BM_REGION t where t.parentcode= :parentcode and t.grade= :grade
@BmRegionDTO.with.ignore.null.page.mapped.entity = org.onetwo.common.jfish.BmRegionDTO
@BmRegionDTO.with.ignore.null.page.ignore.null = true

@BmRegionDTO.delete.all = delete from BM_REGION

@TUser.delete = delete from T_USER where id = ?
@TUser.insert = insert into T_USER(id, user_name) values (:id, :userName)
