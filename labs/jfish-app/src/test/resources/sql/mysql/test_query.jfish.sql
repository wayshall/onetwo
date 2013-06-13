
@BmRegionDTO.find.10 =  select t.* from BM_REGION t order by ? limit 0, 10
@BmRegionDTO.find.10.mapped.entity = org.onetwo.common.jfish.BmRegionDTO
@BmRegionDTO.find.10.ignore.null = true

--searchPageListSupplier_json sql 

@searchPageListSupplier_json = 
	select spr.card_free,spr.price,supp.LOGO_RSURL,supp.Name,supp.feature,sdi.NOT_ENOUGH_OWNER_DISCOUNT,sdi.Adult_Discount from 
	       ( 
	         select * from zjk_supplier_discount where id in( 
	            select max(sdi.id) 
	                   from zjk_supplier_discount sdi 
	                        where discount_type = '2' and END_TIME >= sysdate group by sdi.Product_Id 
	          ) 
	       ) sdi 
	LEFT join Zjk_Supplier_Product spr 
	     on spr.id = sdi.product_id 
	LEFT join ZJK_SUPPLIER supp 
	     on supp.id = spr.supplier_id 
	WHERE 
	     supp.Add_City = :addCity 
	     AND 
	     supp.state = :state 
	     AND 
	     supp.Type_Id = :typeID
@searchPageListSupplier_json.ignore.null=true
@searchPageListSupplier_json.count.sql=select count(*) from zjk_supplier_discount

--searchPageListSupplier_json2 sql 
@searchPageListSupplier_json2 = 
							select spr.card_free,spr.price,supp.LOGO_RSURL,supp.Name,supp.feature,sdi.NOT_ENOUGH_OWNER_DISCOUNT,sdi.Adult_Discount from 
							       ( 
							         select * from zjk_supplier_discount where id in( 
							            select max(sdi.id) 
							                   from zjk_supplier_discount sdi 
							                        where discount_type = '2' and END_TIME >= sysdate group by sdi.Product_Id 
							          ) 
							       ) sdi 
							LEFT join Zjk_Supplier_Product spr 
							     on spr.id = sdi.product_id 
							LEFT join ZJK_SUPPLIER supp 
							     on supp.id = spr.supplier_id 
							WHERE 
							     supp.Add_City = :addCity 
							     AND 
							     supp.state = :state 
							     AND 
							     supp.Type_Id = :typeID
@searchPageListSupplier_json2.ignore.null=true

-------test sql 
@test= select * from table 
					where aa=:bb
					and cc=:dd
					
@test.ignore.null=true




