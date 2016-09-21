select 
		accountdate as accountdate,
		linename as linename,busnumber as busnumber,sum(total) as total,sum(times) as times,naturename as naturename,
		sum(fee_total) as fee_total
	from (
			(
				select 
						rtcs.accountdate as accountdate,
					bb.busnumber as busnumber,sum(rtcs.opfare) as total,count(*) as times,bbn.naturename as naturename,
					bbn.id as natureid,bbl.linename as linename,bbl.lineid as lineid,
					sum(rtcs.opfare) as fee_total
			    from 
			    	rec_city_consume_success rtcs
			  	left join 
				  	base_busline bbl on bbl.lineid=rtcs.buslineid 
				left join 
				  	base_bus bb  on rtcs.busid=bb.busno 
				left join 
				  	base_bus_nature bbn on bbl.busnature=bbn.id 
				where 
					rtcs.accountdate>=convert(datetime, '2016-09-11') 
					and rtcs.accountdate<convert(datetime, '2016-09-11') +1 
				    	and bbl.lineid = :lineId 
			    group by
			  		  rtcs.accountdate
			)
		union all
			(
				select 
						rtcs.accountdate as accountdate,
					bb.busnumber as busnumber,sum(rtcs.electronopfare) as total,count(*) as times,bbn.naturename as naturename,
					bbn.id as natureid,bbl.linename as linename,bbl.lineid as  lineid,
					sum(rtcs.electronopfare) as fee_total
			    from 
			    	rec_thirdcard_consume_success rtcs
				left join 
				  	base_busline bbl on bbl.lineid=rtcs.buslineid 
				left join 
				  	base_bus bb  on rtcs.busid=bb.busno 
				left join 
				  	base_bus_nature bbn on bbl.busnature=bbn.id  
				where 
					rtcs.accountdate>=convert(datetime, '2016-09-11')+1 
					and rtcs.accountdate<convert(datetime, '2016-09-11') +2 
					and rtcs.cardownbusiness in (1, 2) 
				    	and bbl.lineid = :lineId 
			    group by 
  						rtcs.accountdate,
			    	bbl.linename,bbl.lineid,bb.busnumber,bbn.id,bbn.naturename
			)
	) type10   
  	group by 
			accountdate,
  		linename,busnumber,lineid,natureid,naturename
    order by 
      	natureid,linename,busnumber