/***
 * @name: findAppPermissionsByUserId
 * @parser: template
 * 
 */
    select 
      ap.code,
      ap.ptype,
      ap.data_from,
      case 
        when ap.url is not null then concat(ifnull(aa.base_url, ''), ap.url)
        else null 
      end as url,
      ap.method,
      ap.parent_code,
      ap.name,
      ap.sort,
      ap.hidden,
      ap.children_size,
      ap.app_code,
      ap.resources_pattern
    from 
        admin_permission ap
    left join 
       admin_application aa on ap.app_code=aa.code
    left join 
        admin_role_permission arp on arp.PERMISSION_CODE=ap.CODE
    left join 
        admin_user_role aur on aur.ROLE_ID = arp.ROLE_ID
    where aur.USER_ID =:userId and ap.hidden=0
    [#if appCode?has_content]
    and ap.APP_CODE=:appCode
    [/#if]


/***
 * @name: findAppPermissions
 * @parser: template
 * 
 */
    select 
      ap.code,
      ap.ptype,
      ap.data_from,
      case 
        when ap.url is not null then concat(ifnull(aa.base_url, ''), ap.url)
        else null 
      end as url,
      ap.method,
      ap.parent_code,
      ap.name,
      ap.sort,
      ap.hidden,
      ap.children_size,
      ap.app_code,
      ap.resources_pattern
    from 
        admin_permission ap
    left join 
       admin_application aa on ap.app_code=aa.code
    [#if appCode?has_content]
    where
         ap.APP_CODE=:appCode
    [/#if]
    order by 
        sort asc

/***
 * @name: findPermissions
 * @parser: template
 * 
 */
  select 
      ap.code,
      ap.ptype,
      ap.data_from,
      case 
        when ap.url is not null then concat(ifnull(aa.base_url, ''), ap.url)
        else null 
      end as url,
      ap.method,
      ap.parent_code,
      ap.name,
      ap.sort,
      ap.hidden,
      ap.children_size,
      ap.app_code,
      ap.resources_pattern
    from 
        admin_permission ap
    left join 
       admin_application aa on ap.app_code=aa.code
   
   [#if codes??]
    where
        ap.code in (
	        [@foreach list=codes joiner=', '; code, index]
	            #{code}
	        [/@foreach]
        )
   [/#if]

/***
 * @name: findAppPermissionsByRoleIds
 * @parser: template
 * 
 */
    select 
        ap.CODE, ap.url, ap.name, ap.sort
    from 
        admin_permission ap
    left join 
        admin_role_permission arp on arp.PERMISSION_CODE=ap.CODE
    where arp.ROLE_ID = #{roleId} and ap.hidden=0
    [#if appCode?has_content]
    and ap.APP_CODE=:appCode
    [/#if]

/***
 * @name: deleteRolePermissions
 * @parser: template
 * 
 */
    delete from admin_role_permission where PERMISSION_CODE = :permissionCode
    
    
    
