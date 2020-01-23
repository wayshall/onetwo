<#assign requestPath="${_globalConfig.requestModulePath}/${_tableContext.propertyName}"/>
<#assign pagePath="${_globalConfig.requestModulePath}/${_tableContext.tableNameWithoutPrefix?replace('_', '-')}"/>

<#assign servicePackage="${_globalConfig.javaModulePackage}.service"/>
<#assign serviceImplPackage="${_globalConfig.javaModulePackage}.service.impl"/>
<#assign daoPackage="${_globalConfig.javaModulePackage}.dao"/>
<#assign entityPackage="${_globalConfig.javaModulePackage}.entity"/>

<#assign entityClassName="${_tableContext.className}Entity"/>
<#assign entityClassName2="${_tableContext.className}"/>
<#assign serviceImplClassName="${_tableContext.className}ServiceImpl"/>
<#assign serviceImplPropertyName="${_tableContext.propertyName}Service"/>
<#assign mapperClassName="${_tableContext.className}Mapper"/>
<#assign mapperPropertyName="${_tableContext.propertyName}Mapper"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>


package ${_globalConfig.getJavaLocalPackage(_tableContext.localPackage)};

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.controller.DateInitBinder;
import org.onetwo.common.utils.Page;
import org.onetwo.easyui.EasyDataGrid;
import org.onetwo.easyui.EasyViews.EasyGridView;
import org.onetwo.boot.core.web.view.XResponseView;
import org.onetwo.easyui.PageRequest;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.onetwo.common.utils.map.MappableMap;
import org.onetwo.easyui.EasyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.onetwo.common.spring.validator.ValidatorUtils;
import org.onetwo.common.spring.validator.ValidatorUtils.ValidGroup.ValidAnyTime;
import org.onetwo.common.spring.validator.ValidatorUtils.ValidGroup.ValidWhenEdit;
import org.onetwo.common.spring.validator.ValidatorUtils.ValidGroup.ValidWhenNew;

import ${entityPackage}.${entityClassName};
import ${serviceImplPackage}.${serviceImplClassName};

@Controller
@RequestMapping("${requestPath}")
public class ${_tableContext.className}Controller extends ${pluginBaseController} implements DateInitBinder {

    @Autowired
    private ${serviceImplClassName} ${serviceImplPropertyName};
    
    
    @ByPermissionClass(${_tableContext.className}Mgr.class)//在菜单类新建 ${_tableContext.className}Mgr 类后用import
    @RequestMapping(method=RequestMethod.GET)
    @XResponseView(value="easyui", wrapper=EasyGridView.class)
    public ModelAndView index(PageRequest pageRequest, ${entityClassName} ${_tableContext.propertyName}){
        //responsePageOrData不加json后缀访问时返回页面${pagePath}-index，加.json后缀时会返回page对象，并通过EasyGridView包装为适合easyui的数据格式
        return responsePageOrData("${pagePath}-index", ()->{
                    Page<${entityClassName}> page = ${serviceImplPropertyName}.findPage(pageRequest.toPageObject(), ${_tableContext.propertyName});
                    return page;
                });
    }
    
    @ByPermissionClass(${_tableContext.className}Mgr.Create.class)
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView create(@Validated({ValidAnyTime.class, ValidWhenNew.class}) ${entityClassName} ${_tableContext.propertyName}, BindingResult br){
    	ValidatorUtils.throwIfHasErrors(br, true);
        ${serviceImplPropertyName}.save(${_tableContext.propertyName});
        return messageMv("保存成功！");
    }

    @ByPermissionClass(${_tableContext.className}Mgr.class)
    @RequestMapping(value="{${idName}}", method=RequestMethod.GET)
    public ModelAndView show(@PathVariable("${idName}") ${idType} ${idName}){
        ${entityClassName} ${_tableContext.propertyName} = ${serviceImplPropertyName}.findById(${idName});
        return responseData(${_tableContext.propertyName});
    }
    
    @ByPermissionClass(${_tableContext.className}Mgr.Update.class)
    @RequestMapping(value="{${idName}}", method=RequestMethod.PUT)
    public ModelAndView update(@PathVariable("${idName}") ${idType} ${idName}, @Validated({ValidAnyTime.class, ValidWhenEdit.class}) ${entityClassName} ${_tableContext.propertyName}, BindingResult br){
    	ValidatorUtils.throwIfHasErrors(br, true);
        ${_tableContext.propertyName}.set${idName?cap_first}(${idName});
        ${serviceImplPropertyName}.update(${_tableContext.propertyName});
        return messageMv("更新成功！");
    }
    
    
    @ByPermissionClass(${_tableContext.className}Mgr.Delete.class)
    @RequestMapping(method=RequestMethod.DELETE)
    public ModelAndView deleteBatch(${idType}[] ${idName}s){
        ${serviceImplPropertyName}.removeByIds(${idName}s);
        return messageMv("删除成功！");
    }
}
