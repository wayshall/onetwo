@testParserQuery = 
	select id from ( ${template['subsql']} )
@testParserQuery.template.subsql = select id from tableName where userName like '%${userName}'
@testParserQuery.parser = template


@testParserQuery2 = 
	select
		* 
	from 
		tableName2 t 
	where 
		t.id in ( ${template['testParserQuery.template.subsql']} )
@testParserQuery2.template.subsql = select * from tableName
@testParserQuery2.parser = template


