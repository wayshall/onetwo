<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.className}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

<#assign servicePackage="${_globalConfig.javaModulePackage}.service"/>
<#assign serviceImplPackage="${_globalConfig.javaModulePackage}.impl.service"/>
<#assign daoPackage="${_globalConfig.javaModulePackage}.dao"/>
<#assign entityPackage="${_globalConfig.javaModulePackage}.entity"/>

<#assign pageRequestPackage="${_globalConfig.javaModulePackage}.vo.request"/>
<#assign updateRequestPackage="${_globalConfig.javaModulePackage}.vo.request"/>
<#assign voPackage="${_globalConfig.javaModulePackage}.vo"/>

<#assign pageRequestClassName="${_tableContext.className}PageRequest"/>
<#assign voClassName="${_tableContext.className}VO"/>
<#assign updateRequestClassName="${_tableContext.className}UpdateRequest"/>

<#assign entityClassName="${entityClassName!(_tableContext.className+'Entity')}"/>
<#assign entityClassName2="${_tableContext.className}"/>
<#assign serviceClassName="${_tableContext.className}Service"/>
<#assign serviceImplClassName="${_tableContext.className}ServiceImpl"/>
<#assign serviceImplPropertyName="${_tableContext.propertyName}ServiceImpl"/>
<#assign mapperClassName="${_tableContext.className}Mapper"/>
<#assign mapperPropertyName="${_tableContext.propertyName}Mapper"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>

<#assign formFields=DUIEntityMeta.formFields/>
<#assign hasFileFormField=false/>

package ${_globalConfig.getJavaLocalPackage(_tableContext.localPackage)};

import org.onetwo.common.convert.Types;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.utils.Page;
import org.onetwo.common.spring.copier.CopyUtils;
import org.onetwo.dbm.core.internal.DbmCrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<#if DUIEntityMeta?? && DUIEntityMeta.isTree()==true>
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.onetwo.common.tree.DefaultTreeModel;
import org.onetwo.common.tree.TreeBuilder;
import org.onetwo.common.tree.TreeModelCreator;
import org.onetwo.common.tree.TreeUtils;
</#if>

import ${entityPackage}.${entityClassName};
import ${servicePackage}.${serviceImplClassName};
import ${pageRequestPackage}.${pageRequestClassName};
import ${updateRequestPackage}.${updateRequestClassName};
import ${voPackage}.${voClassName};

@Service
@Transactional
public class ${serviceClassName} {
<#if DUIEntityMeta?? && DUIEntityMeta.isTree()==true>
    private static TreeModelCreator<DefaultTreeModel, ${entityClassName}> TREE_MODEL_CREATER = data -> {
        DefaultTreeModel tm = new DefaultTreeModel(data.get${idName?cap_first}(), data.getName(), data.getParent${idName?cap_first}());
        return tm;
    };
</#if>
    
    @Autowired
    private BaseEntityManager baseEntityManager;
<#if DUIEntityMeta?? && DUIEntityMeta.hasFileField()==true>
    @Autowired
    private BootCommonService bootCommonService;
</#if>
    
<#if DUIEntityMeta?? && DUIEntityMeta.isTree()==true>
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
    public Page<${voClassName}> findPage(${pageRequestClassName} pageRequest) {
        return baseEntityManager.from(${entityClassName}.class)
                                .where()
                                    .addFields(pageRequest)
                                    .ignoreIfNull()
                                .end()
                                .toQuery()
                                .page(pageRequest.toPageObject())
                                .mapToNewPage(e -> e.asBean(${voClassName}.class));
    }

    public ${voClassName} findById(${idType} id) {
        ${entityClassName} entity = baseEntityManager.findById(${entityClassName}.class, id);
        return entity.asBean(${voClassName}.class);
    }
    
    public ${voClassName} persist(${updateRequestClassName} request) {
        ${entityClassName} entity =  CopyUtils.copy(${entityClassName}.class, request);
    <#if DUIEntityMeta??>
      <#list DUIEntityMeta.hasDefaultFields as field>
        if (entity.get${field.column.capitalizePropertyName}()==null) {
            entity.set${field.column.capitalizePropertyName}(Types.asValue("${field.defaultValue}", ${field.column.mappingJavaClassLabel}.class));
        }
      </#list>
    </#if>
    <#list formFields as field><#t>
      <#if field.input.isFileType()==true><#t>
        if (${field.name}File!=null) {
            FileStoredMeta ${field.name}FileMeta = bootCommonService.uploadFile("${moduleName!_tableContext.className}", ${field.name}File);
            entity.set${field.column.capitalizePropertyName}(${field.name}FileMeta.getAccessablePath());
        }
      </#if><#t>
    </#list><#t>
        baseEntityManager.persist(entity);
        return entity.asBean(${voClassName}.class);
    }
    
    public ${voClassName} update(${updateRequestClassName} request) {
        ${entityClassName} entity = baseEntityManager.load(${entityClassName}.class, request.${table.primaryKey.readMethodName}());
        CopyUtils.copy(entity, request);
        baseEntityManager.update(entity);
        return entity.asBean(${voClassName}.class);
    }
    
    public void dymanicUpdate(${entityClassName} entity) {
        baseEntityManager.dymanicUpdate(entity);
    }
    
    @Transactional
    public ${entityClassName} removeById(${idType} id) {
    <#if DUIEntityMeta?? && DUIEntityMeta.editableEntities??>
      <#list DUIEntityMeta.editableEntities as editableEntity>
        baseEntityManager.removeById(${editableEntity.entityClass.name}.class, id);
      </#list>
    </#if>
        return baseEntityManager.removeById(${entityClassName}.class, id);
    }
    
    
    @Transactional
    public Collection<${entityClassName}> removeByIds(${idType}... ids) {
        Collection<${entityClassName}> list = new ArrayList<>(ids.length);
        for (${idType} id : ids) {
            ${entityClassName} e = removeById(id);
            list.add(e);
        }
        return list;
    }
    
}
