SELECT 
    perm.code as authority,
    perm.resources_pattern as resources_pattern
FROM 
    admin_permission perm
WHERE
    perm.resources_pattern is not null and perm.resources_pattern!=''
order by 
    perm.sort