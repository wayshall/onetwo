@@query.creator=#baseEntityManager

--充值月对账单
@getRechargeMonthBill = select companyname,count(case when s.qs = '01' then 1 end) contimes,sum(case when s.qs = '01' then cast(tf as bigint) end) conmoney,
count(*) count,sum(cast(tf as bigint)) trans,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) fees,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) income 
						from (select mc.name as 'companyname',info.recharge_rate,ts.* 
							from merchant_info info,term_recharge_pos pos, merchant_company mc, IN_TRANSACTION_${date} ts 
							where ts.pid=substring(pos.recharge_pos_no,9,20) and pos.merchant_id=info.id
							and ts.tt='02' and ts.status='00' and info.AREA_CODE = :areaCode and mc.id=info.company_id  ) s group by companyname 
@getRechargeMonthBill.parser=template
@getRechargeMonthBill.countSql = select count(*) from (
	select companyname,count(*) count,sum(cast(tf as bigint)) trans,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) fees,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) income 
						from (select mc.name as 'companyname',info.recharge_rate,ts.* 
							from merchant_info info,term_recharge_pos pos, merchant_company mc, IN_TRANSACTION_${date} ts 
							where ts.pid=substring(pos.recharge_pos_no,9,20) and pos.merchant_id=info.id
							and ts.tt='02' and ts.status='00' and info.AREA_CODE = :areaCode and mc.id=info.company_id  ) s group by companyname 
) datas

--月对账汇总
@getRechargeMonthBillTotal = select count(case when qs = '01' then 1 end) contimes,sum(case when qs = '01' then cast(tf as bigint) end) conmoney ,
	 count(*) count,sum(cast(tf as bigint)) trans,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) fees,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) income 
							from merchant_info info,term_recharge_pos pos, merchant_company mc, IN_TRANSACTION_${date} ts 
							where ts.pid=substring(pos.recharge_pos_no,9,20) and pos.merchant_id=info.id
							and ts.tt='02' and ts.status='00' and info.AREA_CODE = :areaCode and mc.id=info.company_id  
@getRechargeMonthBillTotal.parser=template

--充值日对账单
@getRechargeDayBill = select companyname,count(case when s.qs = '01' then 1 end) contimes,sum(case when s.qs = '01' then cast(tf as bigint) end) conmoney,
count(*) count,sum(cast(tf as bigint)) trans,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) fees,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) income from 
      				(select mc.name as 'companyname',info.recharge_rate,ts.* from merchant_info info,term_recharge_pos pos, merchant_company mc,( 
      
          				select * from IN_TRANSACTION_${date} where tim >= '${date1}'+ ' 00:00:00'
          						and tim <= '${date1}'+' 23:59:59'
       
       				) ts where ts.pid=substring(pos.recharge_pos_no,9,20) and  pos.merchant_id=info.id
   					and ts.tt='02' and ts.status='00' and info.AREA_CODE = :areaCode and mc.id=info.company_id  ) s group by companyname 
@getRechargeDayBill.parser=template
@getRechargeDayBill.countSql = select count(*) from (
	select companyname,count(*) count,sum(cast(tf as bigint)) trans,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) fees,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) income from 
      				(select mc.name as 'companyname',info.recharge_rate,ts.* from merchant_info info,term_recharge_pos pos, merchant_company mc,( 
      
          				select * from IN_TRANSACTION_${date} where tim >= '${date1}'+ ' 00:00:00'
          						and tim <= '${date1}'+' 23:59:59'
       
       				) ts where ts.pid=substring(pos.recharge_pos_no,9,20) and  pos.merchant_id=info.id
   					and ts.tt='02' and ts.status='00' and info.AREA_CODE = :areaCode and mc.id=info.company_id  ) s group by companyname 
) datas

--日对账汇总
@getRechargeDayBillTotal = select count(case when qs = '01' then 1 end) contimes,sum(case when qs = '01' then cast(tf as bigint) end) conmoney ,
	 count(*) count,sum(cast(tf as bigint)) trans,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) fees,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) income  from merchant_info info,term_recharge_pos pos, merchant_company mc,( 
      
          				select * from IN_TRANSACTION_${date} where tim >= '${date1}'+ ' 00:00:00'
          						and tim <= '${date1}'+' 23:59:59'
       
       				) ts where ts.pid=substring(pos.recharge_pos_no,9,20) and  pos.merchant_id=info.id
   					and ts.tt='02' and ts.status='00' and info.AREA_CODE = :areaCode and mc.id=info.company_id  
@getRechargeDayBillTotal.parser=template

--充值选择时间对账单
@getRechargeRangeBill = select companyname,count(*) count,sum(cast(tf as bigint)) trans,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) fees,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) income from 
      				(select mc.name as 'companyname',info.recharge_rate,ts.* from merchant_info info,term_recharge_pos pos, merchant_company mc,( 
      
          				select * from IN_TRANSACTION_${date} where tim >= '${startTime}'+ ' 00:00:00'
          						and tim <= '${endTime}'+' 23:59:59'
       
       				) ts where ts.pid=substring(pos.recharge_pos_no,9,20) and  pos.merchant_id=info.id
   					and ts.tt='02' and ts.status='00' and info.AREA_CODE = :areaCode and mc.id=info.company_id  ) s group by companyname 
@getRechargeRangeBill.parser=template
@getRechargeRangeBill.countSql = select count(*) from (
	select companyname,count(*) count,sum(cast(tf as bigint)) trans,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) fees,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) income from 
      				(select mc.name as 'companyname',info.recharge_rate,ts.* from merchant_info info,term_recharge_pos pos, merchant_company mc,( 
      
          				select * from IN_TRANSACTION_${date} where tim >= '${startTime}'+ ' 00:00:00'
          						and tim <= '${endTime}'+' 23:59:59'
       
       				) ts where ts.pid=substring(pos.recharge_pos_no,9,20) and  pos.merchant_id=info.id
   					and ts.tt='02' and ts.status='00' and info.AREA_CODE = :areaCode and mc.id=info.company_id  ) s group by companyname 
) datas

--充值月明细单
@getRechargeMonthDetails = select mc.name as 'companyname',info.name as 'name',ts.* from merchant_info info,term_recharge_pos pos, merchant_company mc,IN_TRANSACTION_${date}
					  ts where ts.pid=substring(pos.recharge_pos_no,9,20) and pos.merchant_id=info.id and ts.tt='02' and ts.status='00' and info.AREA_CODE = :areaCode and mc.id=info.company_id 
					  order by ts.tim desc 
@getRechargeMonthDetails.parser=template

--充值日明细单
@getRechargeDayDetails = select mc.name as 'companyname',info.name as 'name',ts.* from merchant_info info,term_recharge_pos pos, merchant_company mc,( 

 						select * from IN_TRANSACTION_${date} where tim >= '${date1}'+ ' 00:00:00'
          						and tim <= '${date1}'+' 23:59:59'
          					
					  )  ts where ts.pid=substring(pos.recharge_pos_no,9,20) and pos.merchant_id=info.id and ts.tt='02' and ts.status='00' and info.AREA_CODE = :areaCode and mc.id=info.company_id 
					  order by ts.tim desc 
@getRechargeDayDetails.parser=template

--充值选择时间明细单
@getRechargeRangeDetails = select mc.name as 'companyname',info.name as 'name',ts.* from merchant_info info,term_recharge_pos pos, merchant_company mc,( 

 						select * from IN_TRANSACTION_${date} where tim >= '${startTime}'+ ' 00:00:00'
          						and tim <= '${endTime}'+' 23:59:59'
          					
					  )  ts where ts.pid=substring(pos.recharge_pos_no,9,20) and pos.merchant_id=info.id and ts.tt='02' and ts.status='00' and info.AREA_CODE = :areaCode and mc.id=info.company_id 
					  order by ts.tim desc 
@getRechargeRangeDetails.parser=template

--本地商户充值月对账单
@getLocalMerchantRechargeMonthBill = select name as 'name',count(case when s.qs = '01' then 1 end) contimes,sum(case when s.qs = '01' then cast(tf as bigint) end) conmoney,
count(*) count,sum(cast(tf as bigint)) trans,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) fees,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) income 
						from (select info.name as 'name',info.recharge_rate,ts.* 
							from merchant_info info,term_recharge_pos pos, IN_TRANSACTION_${date} ts 
							where ts.pid=substring(pos.recharge_pos_no,9,20) and pos.merchant_id=info.id
							and ts.tt='02' and ts.status='00' and info.id = :merchantId  ) s group by name 
@getLocalMerchantRechargeMonthBill.parser=template
@getLocalMerchantRechargeMonthBill.countSql = select count(*) from (
	select name 'name',count(*) count,sum(cast(tf as bigint)) trans,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) fees,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) income 
						from (select info.name as 'name',info.recharge_rate,ts.* 
							from merchant_info info,term_recharge_pos pos, IN_TRANSACTION_${date} ts 
							where ts.pid=substring(pos.recharge_pos_no,9,20) and pos.merchant_id=info.id
							and ts.tt='02' and ts.status='00' and info.id = :merchantId  ) s group by name 
) datas

--本地商户充值日对账单
@getLocalMerchantRechargeDayBill = select name as 'name',count(case when s.qs = '01' then 1 end) contimes,sum(case when s.qs = '01' then cast(tf as bigint) end) conmoney,
count(*) count,sum(cast(tf as bigint)) trans,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) fees,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) income from 
      				(select info.name as 'name',info.recharge_rate,ts.* from merchant_info info,term_recharge_pos pos,( 
      
          				select * from IN_TRANSACTION_${date} where tim >= '${date1}'+ ' 00:00:00'
          						and tim <= '${date1}'+' 23:59:59'
       
       				) ts where ts.pid=substring(pos.recharge_pos_no,9,20) and  pos.merchant_id=info.id
   					and ts.tt='02' and ts.status='00' and info.id = :merchantId  ) s group by name 
@getLocalMerchantRechargeDayBill.parser=template
@getLocalMerchantRechargeDayBill.countSql = select count(*) from (
	select name as 'name',count(*) count,sum(cast(tf as bigint)) trans,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) fees,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) income from 
      				(select info.name as 'name',info.recharge_rate,ts.* from merchant_info info,term_recharge_pos pos,( 
      
          				select * from IN_TRANSACTION_${date} where tim >= '${date1}'+ ' 00:00:00'
          						and tim <= '${date1}'+' 23:59:59'
       
       				) ts where ts.pid=substring(pos.recharge_pos_no,9,20) and  pos.merchant_id=info.id
   					and ts.tt='02' and ts.status='00' and info.id = :merchantId  ) s group by name 
) datas

--本地商户充值月明细单
@getLocalMerchantRechargeMonthDetails = select info.name as 'name',ts.* from merchant_info info,term_recharge_pos pos,IN_TRANSACTION_${date}
					  ts where ts.pid=substring(pos.recharge_pos_no,9,20) and pos.merchant_id=info.id and ts.tt='02' and ts.status='00' and info.id = :merchantId 
					  order by ts.tim desc 
@getLocalMerchantRechargeMonthDetails.parser=template

--本地商户充值日明细单
@getLocalMerchantRechargeDayDetails = select info.name as 'name',ts.* from merchant_info info,term_recharge_pos pos,( 

 						select * from IN_TRANSACTION_${date} where tim >= '${date1}'+ ' 00:00:00'
          						and tim <= '${date1}'+' 23:59:59'
          					
					  )  ts where ts.pid=substring(pos.recharge_pos_no,9,20) and pos.merchant_id=info.id and ts.tt='02' and ts.status='00' and info.id = :merchantId 
					  order by ts.tim desc 
@getLocalMerchantRechargeDayDetails.parser=template

--本地商户充值对账单,按时间选择范围查询
@getLocalMerchantRechargeRangeBill = select name as 'name',count(case when s.qs = '01' then 1 end) contimes,sum(case when s.qs = '01' then cast(tf as bigint) end) conmoney,
count(*) count,sum(cast(tf as bigint)) trans,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) fees,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) income from 
      				(select info.name as 'name',info.recharge_rate,ts.* from merchant_info info,term_recharge_pos pos,( 
      
          				select * from IN_TRANSACTION_${date} where tim >= '${startTime}'+ ' 00:00:00'
          						and tim <= '${endTime}'+' 23:59:59'
       
       				) ts where ts.pid=substring(pos.recharge_pos_no,9,20) and  pos.merchant_id=info.id
   					and ts.tt='02' and ts.status='00' and info.id = :merchantId  ) s group by name 
@getLocalMerchantRechargeRangeBill.parser=template
@getLocalMerchantRechargeRangeBill.countSql = select count(*) from (
	select name as 'name',count(*) count,sum(cast(tf as bigint)) trans,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) fees,sum(cast(tf as bigint)*(cast(recharge_rate as bigint))) income from 
      				(select info.name as 'name',info.recharge_rate,ts.* from merchant_info info,term_recharge_pos pos,( 
      
          				select * from IN_TRANSACTION_${date} where tim >= '${startTime}'+ ' 00:00:00'
          						and tim <= '${endTime}'+' 23:59:59'
       
       				) ts where ts.pid=substring(pos.recharge_pos_no,9,20) and  pos.merchant_id=info.id
   					and ts.tt='02' and ts.status='00' and info.id = :merchantId  ) s group by name 
) datas

--本地商户充值明细单,按时间选择范围查询
@getLocalMerchantRechargeRangeDetails = select info.name as 'name',ts.* from merchant_info info,term_recharge_pos pos,( 

 						select * from IN_TRANSACTION_${date} where tim >= '${startTime}'+ ' 00:00:00'
          						and tim <= '${endTime}'+' 23:59:59'
          					
					  )  ts where ts.pid=substring(pos.recharge_pos_no,9,20) and pos.merchant_id=info.id and ts.tt='02' and ts.status='00' and info.id = :merchantId 
					  order by ts.tim desc 
@getLocalMerchantRechargeRangeDetails.parser=template