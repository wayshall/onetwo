
/****
 * @name: statisActiveByDatePage
 **/
SELECT
	pa.ACTIVE_DATE, SUM(pa.ACTIVE_AMOUNT) as amount
FROM
	product_active pa
GROUP BY
	pa.ACTIVE_DATE
	

/****
 * @name: statisActive
 **/
SELECT
	'汇总' as ACTIVE_DATE, SUM(pa.ACTIVE_AMOUNT) as amount
FROM
	product_active pa
	

/****
 * @name: statisTotalPay
 **/
SELECT 
	sum(pact.ACTIVE_AMOUNT * prd.PRICE) as total
FROM
	product_active pact
LEFT JOIN
	product prd on prd.ID=pact.PRODUCT_ID
	

/****
 * @name: statisTotalIncome
 **/
SELECT 
	sum(pinc.INCOME) as total
FROM
	product_income pinc

/****
 * @name: statisActiveIncomeByGroup
 **/
	
SELECT
	pact.ACTIVE_DATE,
	au.USER_NAME,
	prd.`NAME` as product_name,
	pact.ACTIVE_AMOUNT,
	pact.PRODUCT_PRICE,
	pact.ACTIVE_AMOUNT * pact.PRODUCT_PRICE as income,
	pact.REMARK
FROM
	product_active pact
LEFT JOIN
	product prd ON prd.ID=pact.PRODUCT_ID
LEFT JOIN
	admin_user au on au.ID = pact.ACTIVE_USER_ID
WHERE
	pact.ACTIVE_USER_ID = :userId
	[#if hasSubUser]
	OR au.BELONG_TO_USER_ID = :userId
	[/#if]
GROUP BY
	pact.ACTIVE_DATE,
	pact.ACTIVE_USER_ID,
	pact.PRODUCT_ID
	
/****
 * @name: statisActiveIncomeTotal
 **/
SELECT
	'汇总' as ACTIVE_DATE,
	sum(pact.ACTIVE_AMOUNT) as ACTIVE_AMOUNT,
	sum(pact.ACTIVE_AMOUNT * pact.PRODUCT_PRICE) as income
FROM
	product_active pact
LEFT JOIN
	admin_user au on au.ID = pact.ACTIVE_USER_ID
WHERE
	pact.ACTIVE_USER_ID = :userId
	[#if hasSubUser]
	OR au.BELONG_TO_USER_ID = :userId
	[/#if]

	