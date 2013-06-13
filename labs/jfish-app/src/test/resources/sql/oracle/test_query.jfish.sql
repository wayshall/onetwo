
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


@cause.sql = SELECT   *
											FROM (SELECT l.lottery_date, u.belong_region, l.mobile, l.user_name,
											                 p.lottery_prize_name,
											                 CASE
											                    WHEN u.product_id = '120101592010000006169'
											                       THEN '免费'
											                    ELSE '收费'
											                 END AS TYPE,
											                 l.state
											            FROM yzl_lottery_list l LEFT JOIN uc_user u
											                 ON l.mobile = u.user_moblie
											                 LEFT JOIN yzl_lottery_result_prize p
											                 ON l.lottery_result_id = p.lottery_result_id
											                 ) t
											   WHERE t.lottery_date >= :start_time
											     AND t.lottery_date <= :end_time
											     AND t.belong_region LIKE :region
											     AND t.mobile = :mobile
											     AND t.lottery_prize_name LIKE :prize
											     AND t.user_name = :name
											     AND TYPE = :type
											     AND t.state = :state


@cause.sql.ignore.null=true

@select.insert.sql = INSERT INTO sms_message
									            (ID, phone_nos, title, MESSAGE, plan_send_time, from_app, state,
									             sms_type, create_time)
									   SELECT seq_sms_message.NEXTVAL, u.user_moblie, :title, :content, SYSDATE,
									          'db_sql', -2, :sms_type, SYSDATE
									     FROM uc_user u
									    WHERE u.state IN (1, 50)
									      AND u.state = :state
									      AND u.product_id = :product_id
									      AND u.belong_region = :belong_region
@select.insert.sql.ignore.null=true


@param.like.sql=SELECT   *
											    FROM (SELECT l.lottery_date, u.belong_region, l.mobile, l.user_name,
											                 p.lottery_prize_name,
											                 CASE
											                    WHEN u.product_id = '120101592010000006169'
											                       THEN '免费'
											                    ELSE '收费'
											                 END AS TYPE,
											                 CASE
																WHEN l.state = 0
																	THEN '待抽奖'
																WHEN l.state = 1
																	THEN '已抽奖'
																WHEN l.state = 2
																	THEN '中奖'
															END AS state
											            FROM yzl_lottery_list l LEFT JOIN uc_user u
											                 ON l.mobile = u.user_moblie
											                 LEFT JOIN yzl_lottery_result_prize p
											                 ON l.lottery_result_id = p.lottery_result_id
											                 ) t
											   WHERE t.lottery_date >= :start_time
											     AND t.lottery_date <= :end_time
											     AND t.belong_region LIKE :region
											     AND :mobile LIKE CONCAT ('%', CONCAT (t.mobile, '%'))
											     AND t.lottery_prize_name LIKE :prize
											     AND t.user_name = :name
											     AND TYPE = :type
											     AND t.state = :state
											     
@param.like.sql.ignore.null=true


@BmRegionDTO.find.10 = select * from ( 
						select t.* from BM_REGION t 
					) where rownum <= 10
@BmRegionDTO.find.10.mapped.entity = org.onetwo.common.jfish.BmRegionDTO
