<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.className}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

<#assign servicePackage="${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.${_tableContext.localPackage}"/>
<#assign serviceImplClassName="${_tableContext.className}ServiceImpl"/>
<#assign serviceImplPropertyName="${_tableContext.propertyName}ServiceImpl"/>
<#assign mapperClassName="${_tableContext.className}Mapper"/>
<#assign mapperPropertyName="${_tableContext.propertyName}Mapper"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>

package ${servicePackage};

import org.onetwo.common.db.BaseCrudServiceImpl;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.entity.${_tableContext.className};

@Service
@Transactional
public class ${serviceImplClassName} extends BaseCrudServiceImpl<${_tableContext.className}, ${idType}> {

    @Autowired
    private BaseEntityManager baseEntityManager;

    
    @Override
    public BaseEntityManager getBaseEntityManager() {
		return baseEntityManager;
	}
	
    public void findPage(Page<${_tableContext.className}> page, ${_tableContext.className} ${_tableContext.propertyName}){
        Querys.from(baseEntityManager, ${_tableContext.className}.class)
        		.where()
        		.addFields(${_tableContext.propertyName})
        		.ignoreIfNull()
        		.end()
        		.toQuery()
        		.page(page);
    }
    
}
