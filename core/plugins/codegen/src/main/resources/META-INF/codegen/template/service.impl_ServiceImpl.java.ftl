package ${fullPackage};
<#--
<#assign serviceInterfaceName = table.className+"Service"/>
import ${basePackage+".service."+serviceInterfaceName};
-->
<#assign entityName = commonName+"Entity"/>
import org.springframework.stereotype.Service;
import org.onetwo.common.hibernate.HibernateCrudServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import ${modulePackage+".entity."+entityName};

@Transactional
@Service
public class ${selfClassName} extends HibernateCrudServiceImpl<${entityName}, ${table.primaryKey.javaType.simpleName}> {

}
