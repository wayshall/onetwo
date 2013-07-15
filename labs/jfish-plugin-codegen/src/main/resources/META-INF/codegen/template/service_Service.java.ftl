package ${fullPackage};

<#assign entityName = table.className+"Entity"/>
import ${modulePackage+".entity."+entityName};

import org.onetwo.common.db.CrudEntityManager;

public interface ${selfClassName} extends CrudEntityManager<${entityName}, ${table.primaryKey.javaType.simpleName}> {

}
