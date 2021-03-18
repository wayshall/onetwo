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

import java.util.Collection;

import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ${entityPackage}.${entityClassName};

@Service
@Transactional
public class ${serviceImplClassName} {

    @Autowired
    private BaseEntityManager baseEntityManager;
    
    public Page<${entityClassName}> findPage(Page<${entityClassName}> page, ${entityClassName} ${_tableContext.propertyName}){
        return baseEntityManager.from(${entityClassName}.class)
                	.where()
            		  .addFields(${_tableContext.propertyName})
            		  .ignoreIfNull()
            		.end()
            		.toQuery()
            		.page(page);
    }
    
    public ${entityClassName} save(${entityClassName} entity) {
		baseEntityManager.persist(entity);
		return entity;
	}

	public void update(${entityClassName} entity) {
		baseEntityManager.update(entity);
	}
    
    public ${entityClassName} findById(${idType} id) {
		return baseEntityManager.findById(${entityClassName}.class, id);
	}

	public Collection<${entityClassName}> removeByIds(${idType}... id) {
		return baseEntityManager.removeByIds(${entityClassName}.class, id);
	}
}
