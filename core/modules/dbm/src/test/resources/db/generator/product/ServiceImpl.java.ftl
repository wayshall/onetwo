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

import java.util.Collection;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.entity.${_tableContext.className};

@Service
@Transactional
public class ${serviceImplClassName} {

    @Autowired
    private BaseEntityManager baseEntityManager;
    
    public void findPage(Page<${_tableContext.className}> page, ${_tableContext.className} ${_tableContext.propertyName}){
        Querys.from(baseEntityManager, ${_tableContext.className}.class)
        		.where()
        		.addFields(${_tableContext.propertyName})
        		.ignoreIfNull()
        		.end()
        		.toQuery()
        		.page(page);
    }
    
    public void save(${_tableContext.className} entity) {
		baseEntityManager.persist(entity);
	}

	public void update(${_tableContext.className} entity) {
		baseEntityManager.update(entity);
	}
    
    public ${_tableContext.className} findById(${idType} id) {
		return baseEntityManager.findById(${_tableContext.className}.class, id);
	}

	public Collection<${_tableContext.className}> removeByIds(${idType}... id) {
		return baseEntityManager.removeByIds(${_tableContext.className}.class, id);
	}
}
