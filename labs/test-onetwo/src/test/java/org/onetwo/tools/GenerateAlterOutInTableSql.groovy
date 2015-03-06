package org.onetwo.tools

import groovy.time.TimeCategory

def ym = "";
def sql = '''\
	alter table out_transaction${ym} add bus_id bigint \n\
	alter table out_transaction${ym} add card_type bigint\n\
	alter table out_transaction${ym} add CSTACCFC bigint\n\
	alter table out_transaction${ym} alter column line bigint\n\
	alter table out_transaction${ym} alter column pid varchar(16)\n\
	alter table out_transaction${ym} alter column lpid varchar(16)\n\
	\n\n\
	'''
println sql;
Date startDate = Date.parse("yyyyMM", "201310");
use(TimeCategory){
	while(startDate.format("yyyyMM").toInteger()<=201404){
		ym = "_"+startDate.format("yyyyMM");
		//双引号才是gstring，单引号是java的string
		sql = """
		alter table out_transaction${ym} add bus_id bigint \n
		alter table out_transaction${ym} add card_type bigint\n
		alter table out_transaction${ym} add CSTACCFC bigint\n
		alter table out_transaction${ym} alter column line bigint\n
		alter table out_transaction${ym} alter column pid varchar(16)\n
		alter table out_transaction${ym} alter column lpid varchar(16)\n
		\n\n
	"""
		println sql;
		startDate = startDate + 1.month;
	}
}
