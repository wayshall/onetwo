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

import org.onetwo.common.convert.Types;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.utils.Page;
import org.onetwo.dbm.core.internal.DbmCrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<#if DUIEntityMeta.isTree()==true>
import java.util.List;
import java.util.stream.Collectors;
import org.onetwo.common.tree.DefaultTreeModel;
import org.onetwo.common.tree.TreeBuilder;
import org.onetwo.common.tree.TreeModelCreator;
import org.onetwo.common.tree.TreeUtils;
</#if>

import ${entityPackage}.${entityClassName};

@Service
@Transactional
public class ${serviceImplClassName} extends DbmCrudServiceImpl<${entityClassName}, ${idType}> {
<#if DUIEntityMeta.isTree()==true>
    private static TreeModelCreator<DefaultTreeModel, ${entityClassName}> TREE_MODEL_CREATER = data -> {
        DefaultTreeModel tm = new DefaultTreeModel(data.get${idName?cap_first}(), data.getName(), data.getParent${idName?cap_first}());
        return tm;
    };
</#if>
    
    @Autowired
    public ${serviceImplClassName}(BaseEntityManager baseEntityManager) {
        super(baseEntityManager);
    }
    
<#if DUIEntityMeta.isTree()==true>
    public List<DefaultTreeModel> loadAsTree() {
        List<${entityClassName}> treeList = this.baseEntityManager.findAll(${entityClassName}.class);
        TreeBuilder<DefaultTreeModel> treeBuilder = TreeUtils.newBuilder(treeList, TREE_MODEL_CREATER);
        treeBuilder.rootIds(0);
        return treeBuilder.buidTree();
    }
    
    public List<DefaultTreeModel> loadTreeDatas(${entityClassName} ${_tableContext.propertyName}) {
        List<${entityClassName}> treeList = this.baseEntityManager.from(${entityClassName}.class)
                                                            .where()
                                                                .addFields(${_tableContext.propertyName})
                                                            .end()
                                                            .toQuery()
                                                            .list();
        List<DefaultTreeModel> treeModels = treeList.stream().map(d -> {
            return TREE_MODEL_CREATER.createTreeModel(d);
        }).collect(Collectors.toList());
        return treeModels;
    }
</#if>
    
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
    
    public ${entityClassName} save(${entityClassName} entity) {
      <#list DUIEntityMeta.hasDefaultFields as field>
        if (entity.get${field.column.capitalizePropertyName}()==null) {
            entity.set${field.column.capitalizePropertyName}(Types.asValue("${field.defaultValue}", ${field.column.mappingJavaClassLabel}.class));
        }
      </#list>
        baseEntityManager.persist(entity);
        return entity;
    }
    
    @Transactional
    @Override
    public ${entityClassName} removeById(${idType} id) {
    <#if DUIEntityMeta?? && DUIEntityMeta.editableEntities??>
      <#list DUIEntityMeta.editableEntities as editableEntity>
        baseEntityManager.removeById(${editableEntity.entityClass.name}.class, id);
      </#list>
    </#if>
        return super.removeById(id);
    }
    
}
