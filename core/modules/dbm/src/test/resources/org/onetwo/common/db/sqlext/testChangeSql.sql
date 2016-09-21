select
      prd.id,
      ap.sku_id,
      ap.product_name,
      prd.product_no,
      prd.base_type,
      prd.market_price,
      prd.sale_price,
      prd.mem_coin_limit,
      prd.vip_coin_limit,
      nvl(ap.LOGO_RSURL, prd.LOGO_RSURL) as LOGO_RSURL,
      prd.banner_rsurl,
      case when mcht.longitude is null then '' else mcht.longitude||','||mcht.latitude end as merchant_coords,
      prd.merchant_id,
      mcht.merchant_name,
      mcht.address as merchant_address,
      mcht.tel_num as merchant_mobile,
      prd.show_tags as tags,
      
      bhv.praise_count,
      bhv.sale_count,
      bhv.favor_count,
      bhv.view_count
	from 
  		EM_ACTIVITY_PRODUCT ap
  	left join
      em_activity act on act.id=ap.activity_id
	left join
	  em_product prd on ap.product_id = prd.id
	left join
	  em_product_behavior bhv on prd.id=bhv.id
	left join
	  bs_merchant mcht on mcht.id = prd.merchant_id
    left join
      EM_SALE_SCOPE_DETAIL scp on scp.scope_id=act.scope_id
      
 where
      ap.state = 50 and
      scp.application_id=#{param.app_id} and 
      scp.partner_id=#{param.partner_id} and
	  ap.activity_id = #{param.activity_id} 
order by
	ap.id desc
	  
	  
