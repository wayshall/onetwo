package org.onetwo.tools

import groovy.time.TimeCategory


Date startDate = Date.parse("yyyyMM", "201311");
Date endDate = Date.parse("yyyyMM", "201512");
def sqlList = [];
use(TimeCategory){
	while(startDate<=endDate){
		ym = startDate.format("yyyyMM");
		//双引号才是gstring，单引号是java的string
		sql = " select * from IN_TRANSACTION_${ym} "
		sqlList += sql
		startDate = startDate + 1.month;
	}
}
def viewSql = "create view dbo.view_in_transaction \n as \n" + sqlList.join("\n union all \n")
println viewSql