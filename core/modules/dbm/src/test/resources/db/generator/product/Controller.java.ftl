<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.propertyName}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix?replace('_', '-')}"/>

<#assign servicePackage="${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.service"/>
<#assign serviceImplClassName="${_tableContext.className}ServiceImpl"/>
<#assign serviceImplPropertyName="${_tableContext.propertyName}ServiceImpl"/>
<#assign mapperClassName="${_tableContext.className}Mapper"/>
<#assign mapperPropertyName="${_tableContext.propertyName}Mapper"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>


package ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.${_tableContext.localPackage};

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.controller.DateInitBinder;
import org.onetwo.common.utils.Page;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.onetwo.common.utils.map.MappableMap;
import org.onetwo.easyui.EasyModel;
import org.onetwo.easyui.EasyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.entity.${_tableContext.className};
import ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.service.impl.${serviceImplClassName};

@Controller
@RequestMapping("${requestPath}")
public class ${_tableContext.className}Controller extends AbstractBaseController implements DateInitBinder {

    @Autowired
    private ${serviceImplClassName} ${serviceImplPropertyName};
    
    
    @ByPermissionClass(${_tableContext.className}Mgr.class)
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(EasyPage<${_tableContext.className}> easyPage, ${_tableContext.className} ${_tableContext.propertyName}){
        return responsePageOrData("${pagePath}-index", ()->{
        			Page<${_tableContext.className}> page = Page.create(easyPage.getPage(), easyPage.getPageSize());
                    ${serviceImplPropertyName}.findPage(page, ${_tableContext.propertyName});
                    return EasyPage.create(page);
                });
    }
    
    @ByPermissionClass(${_tableContext.className}Mgr.Create.class)
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView create(${_tableContext.className} ${_tableContext.propertyName}){
        ${serviceImplPropertyName}.save(${_tableContext.propertyName});
        return messageMv("保存成功！");
    }
    @ByPermissionClass(${_tableContext.className}Mgr.class)
    @RequestMapping(value="{${idName}}", method=RequestMethod.GET)
    public ModelAndView show(@PathVariable("${idName}") Long ${idName}){
        ${_tableContext.className} ${_tableContext.propertyName} = ${serviceImplPropertyName}.findById(${idName});
        return responseData(${_tableContext.propertyName});
    }
    
    @ByPermissionClass(${_tableContext.className}Mgr.Update.class)
    @RequestMapping(value="{${idName}}", method=RequestMethod.PUT)
    public ModelAndView update(@PathVariable("${idName}") Long ${idName}, ${_tableContext.className} ${_tableContext.propertyName}){
        ${_tableContext.propertyName}.set${idName?cap_first}(${idName});
        ${serviceImplPropertyName}.save(${_tableContext.propertyName});
        return messageMv("更新成功！");
    }
    
    
    @ByPermissionClass(${_tableContext.className}Mgr.Delete.class)
    @RequestMapping(method=RequestMethod.DELETE)
    public ModelAndView deleteBatch(Long[] ${idName}s){
        ${serviceImplPropertyName}.removeByIds(${idName}s);
        return messageMv("删除成功！");
    }
}
