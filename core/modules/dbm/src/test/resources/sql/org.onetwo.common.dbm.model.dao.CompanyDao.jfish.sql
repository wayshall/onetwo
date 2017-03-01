/****
 * dbm sql 文件
 */


/****
 * @name: findCompaniesByLikeName
 */
select 
    comp.id,
    comp.name,
    comp.description,
    comp.employee_number
from
    company comp
where
    comp.name like :name?likeString
    

/****
 * @name: findCompaniesByNames
 */
select 
    comp.id,
    comp.name,
    comp.description,
    comp.employee_number
from
    company comp
[#if names?? && names?size>0]
where
    comp.name in (:names)
[/#if]

/*****
 * @name: findDepartmentsWithComapny
 */
select 
    depart.*,
    comp.id as comp_id,
    comp.`name` as comp_name,
    comp.description as comp_description
from 
    department depart
left join 
    company comp on comp.id=depart.company_id



/*****
 * @name: findNestedDepartments
 */
select 
    depart.*,
    comp.id as comp_id,
    comp.`name` as comp_name,
    comp.description as comp_description,
    emply.name as emply_name,
    emply.join_date as emply_join_date,
    emply.department_id as emply_department_id
from 
    department depart
left join 
    company comp on comp.id=depart.company_id
left join
    employee emply on emply.department_id=depart.id


/*****
 * @name: findNestedDepartmentsWithEmployeeId
 */
select 
    depart.*,
    comp.id as comp_id,
    comp.`name` as comp_name,
    comp.description as comp_description,
    emply.id as emply_id,
    emply.name as emply_name,
    emply.join_date as emply_join_date,
    emply.department_id as emply_department_id
from 
    department depart
left join 
    company comp on comp.id=depart.company_id
left join
    employee emply on emply.department_id=depart.id



/*****
 * @name: findNestedCompanies
 */
select 
    comp.*,
    depart.id as departments_id,
    depart.company_id as departments_company_id,
    depart.`name` as departments_name,
    emply.name as emply_name,
    emply.join_date as emply_join_date,
    emply.department_id as emply_department_id
from 
    company comp
left join 
    department depart on comp.id=depart.company_id
left join
    employee emply on emply.department_id=depart.id



/*****
 * @name: findNestedCompaniesWithDepartmentMap
 */
select 
        comp.*,
    depart.id as departments_id,
        depart.company_id as departments_company_id,
        depart.`name` as departments_name,
    emply.name as emply_name,
    emply.join_date as emply_join_date,
    emply.department_id as emply_department_id
from 
        company comp
left join 
    department depart on comp.id=depart.company_id
left join
    employee emply on emply.department_id=depart.id











