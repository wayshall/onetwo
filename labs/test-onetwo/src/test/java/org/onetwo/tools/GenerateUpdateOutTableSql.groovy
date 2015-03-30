package org.onetwo.tools

import groovy.time.TimeCategory

def ym = "";
def sql = '''\
	\n\n\
	'''
println sql;
Date startDate = Date.parse("yyyyMM", "201401");
use(TimeCategory){
	while(startDate.format("yyyyMM").toInteger()<=201501){
		ym = "_"+startDate.format("yyyyMM");
		//双引号才是gstring，单引号是java的string
		sql = """
		update ot
		set ot.bus_id=33297 from out_transaction${ym} ot
		where ot.bus_id=33406
		and ot.settle_tim>='2014-01-01' and ot.settle_tim<'2014-12-26'
		\n\n
	"""
		println sql;
		startDate = startDate + 1.month;
	}
}
