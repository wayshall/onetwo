@testParserQuery = 
	select id from ( ${template['subsql']} ) where startDate >= convert(datetime, ${_func.dateAs(datestr, 'yyyyMM')})
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


@testParserQuery3 = 
	update sb set aa=bb from batch_user sb where sb.${userName}=:userName;
@testParserQuery.parser = template

@testForeach = 
	select uic.ic_lno from (
		SELECT 
			ui.ic_lno, count(ui.ic_lno) as amount 
		from issue_user_info ui where 
			ui.ic_lno in ([@foreach list=cardNos separator=", "; val]${val}[/@foreach])
		group by ui.ic_lno
	) uic where uic.amount>0
@testForeach.parser = template

@testInParams = 
	select uic.ic_lno from (
		SELECT 
			ui.ic_lno, count(ui.ic_lno) as amount 
		from issue_user_info ui where 
			ui.ic_lno in ( ${_func.inParams('cardNo', cardNos.size())} )
		group by ui.ic_lno
	) uic where uic.amount>0
@testInParams.parser = template



@testNumberParams = 
	select *  from tableName2 t where  t.id = '${id}'
@testParserQuery2.template.subsql = select * from tableName
@testParserQuery2.parser = template

