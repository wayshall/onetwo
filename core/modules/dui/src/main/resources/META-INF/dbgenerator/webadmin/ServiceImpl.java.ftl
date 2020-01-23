<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.className}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

<#assign servicePackage="${_globalConfig.javaModulePackage}.service"/>
<#assign serviceImplPackage="${_globalConfig.javaModulePackage}.impl.service"/>
<#assign daoPackage="${_globalConfig.javaModulePackage}.dao"/>
<#assign entityPackage="${_globalConfig.javaModulePackage}.entity"/>

<#assign entityClassName="${_tableContext.className}Entity"/>
<#assign entityClassName2="${_tableContext.className}"/>
<#assign serviceImplClassName="${_tableContext.className}ServiceImpl"/>
<#assign serviceImplPropertyName="${_tableContext.propertyName}ServiceImpl"/>
<#assign mapperClassName="${_tableContext.className}Mapper"/>
<#assign mapperPropertyName="${_tableContext.propertyName}Mapper"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>

package ${_globalConfig.getJavaLocalPackage(_tableContext.localPackage)};

import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.utils.Page;
import org.onetwo.dbm.core.internal.DbmCrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ${entityPackage}.${entityClassName};

@Service
@Transactional
public class ${serviceImplClassName} extends DbmCrudServiceImpl<${entityClassName}, ${idType}> {
    
    @Autowired
    public ${serviceImplClassName}(BaseEntityManager baseEntityManager) {
        super(baseEntityManager);
    }
    
    @Transactional(readOnly=true)
    public Page<${entityClassName}> findPage(Page<${entityClassName}> page, ${entityClassName} example) {
        return baseEntityManager.from(entityClass)
                                .where()
                                    .addFields(example)
                                    .ignoreIfNull()
                                .end()
                                .toQuery()
                                .page(page);
    }
}
