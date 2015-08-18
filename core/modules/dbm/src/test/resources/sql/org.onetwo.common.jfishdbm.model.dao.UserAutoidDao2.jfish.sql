
/***
 * @name: findNormalUser
 * @parser: template
 */
select 
    usr.*
from 
    test_user_autoid usr
where 
    usr.status = 'NORMAL'
    
    
/***
 * @name: findUserList(NORMAL)
 * @parser: template
 */
select 
    usr.*
from 
    test_user_autoid usr
where 
    usr.status = 'NORMAL'
    
/***
 * @name: findUserList(DELETE)
 * @parser: template
 */
select 
    usr.*
${fragment['subWhere']}
--${fragment['findUserPage.fragment.subWhere']}

    
/***
 *  @name: findUserList(DELETE)
 *  @fragment: subWhere
 */
from 
    test_user_autoid usr
where 
    usr.status = 'DELETE'
    
    

/***
 * @name: findUserPage
 * @parser: template
 */
select 
    usr.*
${fragment['subWhere']}

/***
 *  @name: findUserPage
 *  @property: countSql
 */
select 
    count(1)
${fragment['subWhere']}
    
/***
 *  @name: findUserPage
 *  @fragment: subWhere
 */
from 
    test_user_autoid usr
where 
    user_name like :userName?likeString
    