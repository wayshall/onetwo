package ${fullPackage};
<#--
<#assign serviceInterfaceName = table.className+"Service"/>
import ${basePackage+".service."+serviceInterfaceName};
-->
<#assign entityName = commonName+"Entity"/>
import org.springframework.stereotype.Service;

import ${modulePackage+".entity."+entityName};

import org.onetwo.common.fish.JFishCrudServiceImpl;


@Service
public class ${selfClassName} extends JFishCrudServiceImpl<${entityName}, ${table.primaryKey.javaType.simpleName}> {

}
