package org.onetwo.tools

def sql = "\
select ui.* from issue_user_info ui\
where ui.plan_id not in (\
	${[412..463].join(',')}\
)\
and ui.cert_code in (\
	select u.cert_code from issue_user_info u where u.plan_id in (\
		${[412..463].join(',')}\
	)\
)"

printf sql