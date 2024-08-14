<#assign requestPath="${_globalConfig.requestModulePath}/${_tableContext.propertyName}"/>
<#assign pagePath="${_globalConfig.requestModulePath}/${_tableContext.tableNameWithoutPrefix?replace('_', '-')}"/>

<#assign servicePackage="${_globalConfig.javaModulePackage}.service"/>
<#assign serviceImplPackage="${_globalConfig.javaModulePackage}.service.impl"/>
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
<#assign serviceImplPropertyName="${_tableContext.propertyName}Service"/>
<#assign mapperClassName="${_tableContext.className}Mapper"/>
<#assign mapperPropertyName="${_tableContext.propertyName}Mapper"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>
<#assign formFields=DUIEntityMeta.formFields/>
<#assign hasFileFormField=false/>


package ${_globalConfig.getJavaLocalPackage(_tableContext.localPackage)};

import org.onetwo.boot.core.web.controller.DateInitBinder;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.PageRequest;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.onetwo.ext.permission.api.PermissionType;
import org.onetwo.ext.permission.api.annotation.PermissionMeta;
import org.onetwo.boot.core.web.service.BootCommonService;

import org.onetwo.common.data.DataResult;
import org.onetwo.common.data.Result;
import org.onetwo.common.file.FileStoredMeta;
import org.onetwo.common.spring.mvc.utils.DataResults;

<#if DUIEntityMeta.isTree()==true>
import java.util.List;
import org.onetwo.common.tree.DefaultTreeModel;
import org.onetwo.common.tree.TreeBuilder;
import org.onetwo.common.tree.TreeModelCreator;
import org.onetwo.common.tree.TreeUtils;
</#if>

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.onetwo.common.spring.validator.ValidatorUtils;
import org.onetwo.common.spring.validator.ValidatorUtils.ValidGroup.ValidAnyTime;
import org.onetwo.common.spring.validator.ValidatorUtils.ValidGroup.ValidWhenEdit;
import org.onetwo.common.spring.validator.ValidatorUtils.ValidGroup.ValidWhenNew;

<#if DUIEntityMeta?? && DUIEntityMeta.editableEntity==true>
import ${_globalConfig.getJavaLocalPackage(_tableContext.localPackage)}.${DUIEntityMeta.parent.table.className}MgrController.${DUIEntityMeta.parent.table.className}Mgr.Edit${DUIEntityMeta.table.className};
</#if>
import ${entityPackage}.${entityClassName};
import ${servicePackage}.${serviceImplClassName};
import ${pageRequestPackage}.${pageRequestClassName};
import ${updateRequestPackage}.${updateRequestClassName};
import ${voPackage}.${voClassName};

@RestController
public class ${_tableContext.className}MgrController extends ${pluginBaseController} implements DateInitBinder {

    @Autowired
    private ${serviceClassName} ${serviceImplPropertyName};
    
<#if DUIEntityMeta?? && DUIEntityMeta.editableEntity==true>
    @ByPermissionClass(Edit${DUIEntityMeta.table.className}.class)
    @GetMapping(value="${requestPath}/{${idName}}")
    public ${entityClassName} get(@PathVariable("${idName}") ${idType} ${idName}){
        ${entityClassName} ${_tableContext.propertyName} = ${serviceImplPropertyName}.findById(${idName});
        return ${_tableContext.propertyName};
    }
    
    @ByPermissionClass(Edit${DUIEntityMeta.table.className}.class)
    @PostMapping(value="${requestPath}/{${idName}}")
    public DataResult<${entityClassName}> save(@PathVariable("${idName}") ${idType} ${idName}, @Validated ${entityClassName} ${_tableContext.propertyName}, BindingResult br){
        ValidatorUtils.throwIfHasErrors(br, true);
        ${_tableContext.propertyName}.set${idName?cap_first}(${idName});
        ${serviceImplPropertyName}.save(${_tableContext.propertyName});
        return DataResults.<${entityClassName}>success("保存成功！").data(${_tableContext.propertyName}).build();
    }
<#else>
<#if DUIEntityMeta.isTree()==true>
    @ByPermissionClass(value=${_tableContext.className}Mgr.class, overrideMenuUrl=false)
<#else>
    @ByPermissionClass(${_tableContext.className}Mgr.class)//在菜单类新建 ${_tableContext.className}Mgr 类后用import
</#if>
    @GetMapping("${requestPath}")
    public Page<${voClassName}> list(${pageRequestClassName} pageRequest){
        Page<${voClassName}> page = ${serviceImplPropertyName}.findPage(pageRequest);
        return page;
    }
    
  <#if DUIEntityMeta.isTree()==true>
    // 非懒加载
    @ByPermissionClass(${_tableContext.className}Mgr.class)
    @GetMapping(value="${requestPath}Tree")
    public List<DefaultTreeModel> tree(){
        List<DefaultTreeModel> trees = ${serviceImplPropertyName}.loadAsTree();
        return trees;
    }
    
    @ByPermissionClass(${_tableContext.className}Mgr.class)
    @GetMapping(value="${requestPath}LoadTree")
    public List<DefaultTreeModel> loadTree(${entityClassName} ${_tableContext.propertyName}){
        List<DefaultTreeModel> trees = ${serviceImplPropertyName}.loadTreeDatas(${_tableContext.propertyName});
        return trees;
    }
  </#if>
    
    @ByPermissionClass(${_tableContext.className}Mgr.Create.class)
    @PostMapping("${requestPath}")
    public DataResult<${voClassName}> create(@Validated ${updateRequestClassName} request, BindingResult br){
        ValidatorUtils.throwIfHasErrors(br, true);
        ${voClassName} vo = ${serviceImplPropertyName}.persist(request);
        return DataResults.<${voClassName}>success("保存成功！").data(vo).build();
    }

    @ByPermissionClass(${_tableContext.className}Mgr.class)
    @GetMapping(value="${requestPath}/{${idName}}")
    public ${voClassName} get(@PathVariable("${idName}") ${idType} ${idName}){
        ${voClassName} ${_tableContext.propertyName} = ${serviceImplPropertyName}.findById(${idName});
        return ${_tableContext.propertyName};
    }
    
    @ByPermissionClass(${_tableContext.className}Mgr.Update.class)
    @PutMapping(value="${requestPath}/{${idName}}")
    public DataResult<${voClassName}> update(@PathVariable("${idName}") ${idType} ${idName}, 
                                                @Validated ${updateRequestClassName} request, 
                                                    BindingResult br){
        ValidatorUtils.throwIfHasErrors(br, true);
        request.set${idName?cap_first}(${idName});
        ${voClassName} vo = ${serviceImplPropertyName}.update(request);
        return DataResults.<${voClassName}>success("保存成功！").data(vo).build();
    }
    
    
    @ByPermissionClass(${_tableContext.className}Mgr.Delete.class)
    @DeleteMapping("${requestPath}")
    public Result deleteBatch(${idType}[] ${idName}s){
        ${serviceImplPropertyName}.removeByIds(${idName}s);
        return DataResults.success("删除成功！").build();
    }

    /****
     * ${(DUIEntityMeta.label)!table.comments[0]} 权限类
     */
    @PermissionMeta(name="${(DUIEntityMeta.label)!table.comments[0]}管理"<#if DUIEntityMeta.childEntity>, hidden=true</#if>)
    public static interface ${_tableContext.className}Mgr {
    
        @PermissionMeta(name = "新增", permissionType=PermissionType.FUNCTION)
        public interface Create {
        }

        @PermissionMeta(name = "更新", permissionType=PermissionType.FUNCTION)
        public interface Update {
        }

        @PermissionMeta(name = "删除", permissionType=PermissionType.FUNCTION)
        public interface Delete {
        }
        
    <#if DUIEntityMeta?? && DUIEntityMeta.editableEntities??>
      <#list DUIEntityMeta.editableEntities as editableEntity>
        @PermissionMeta(name = "编辑${editableEntity.label}", permissionType=PermissionType.FUNCTION)
        public interface Edit${editableEntity.table.className} {
        }
      </#list>
    </#if>
    }
</#if>
}
