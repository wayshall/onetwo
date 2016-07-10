<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.propertyName}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix?replace('_', '-')?lower_case}"/>

<#assign controllerPackage="${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.controller"/>
<#assign servicePackage="${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.service"/>
<#assign serviceImplClassName="${_tableContext.className}ServiceImpl"/>
<#assign serviceImplPropertyName="${_tableContext.propertyName}ServiceImpl"/>
<#assign mapperClassName="${_tableContext.className}Mapper"/>
<#assign mapperPropertyName="${_tableContext.propertyName}Mapper"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>


package ${controllerPackage};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ${servicePackage}.${serviceImplClassName};
import ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.entity.${_tableContext.className};
import com.yooyo.emall.common.Constants.ValidatorGroups.AllGroup;
import com.yooyo.emall.common.Constants.ValidatorGroups.CreateGroup;
import com.yooyo.emall.common.Constants.ValidatorGroups.UpdateGroup;
import com.yooyo.emall.common.Page;
import com.yooyo.emall.common.RestResult;
import com.yooyo.emall.common.security.AbstractBaseController;
import com.yooyo.emall.utils.ActivityConstants;

@Controller
@RequestMapping(ActivityConstants.MANAGER_BASE_URL+"/${_tableContext.propertyName}")
public class ${_tableContext.className}Controller extends AbstractBaseController {

    @Autowired
    private ${serviceImplClassName} ${serviceImplPropertyName};
    
    
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(Page<${_tableContext.className}> page, ${_tableContext.className} ${_tableContext.propertyName}){
        return mv("${pagePath}-index", 
                        "page", ${serviceImplPropertyName}.findPage(page, ${_tableContext.propertyName}));
    }
    
    @RequestMapping(value="new", method=RequestMethod.GET)
    public ModelAndView _new(@ModelAttribute("${_tableContext.propertyName}")${_tableContext.className} ${_tableContext.propertyName}, BindingResult br){
        return mv("${pagePath}-new");
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView create(@Validated({AllGroup.class, CreateGroup.class}) @ModelAttribute("${_tableContext.propertyName}") ${_tableContext.className} ${_tableContext.propertyName}, BindingResult br){
        if(br.hasErrors()){
            return _new(${_tableContext.propertyName}, br);
        }
        ${serviceImplPropertyName}.save(${_tableContext.propertyName});
        return redirectTo(ActivityConstants.MANAGER_BASE_URL+"/${_tableContext.propertyName}", "保存成功！");
    }
    
    @RequestMapping(value="{id}/edit", method=RequestMethod.GET)
    @ResponseBody
    public ModelAndView edit(@PathVariable("id") Long id){
        ${_tableContext.className} ${_tableContext.propertyName} = ${serviceImplPropertyName}.findById(id);
        return mv("${pagePath}-edit", ${_tableContext.propertyName}, ${_tableContext.propertyName});
    }
    
    @RequestMapping(value="{id}", method=RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Long id, 
                                @Validated({AllGroup.class, UpdateGroup.class}) @ModelAttribute("${_tableContext.propertyName}") ${_tableContext.className} ${_tableContext.propertyName}, 
                                BindingResult br){
        ${_tableContext.propertyName}.setId(id);
        ${serviceImplPropertyName}.update(${_tableContext.propertyName});
        return redirectTo(ActivityConstants.MANAGER_BASE_URL+"/${_tableContext.propertyName}", "更新成功！");
    }
    
    @ResponseBody
    @RequestMapping(method=RequestMethod.DELETE)
    public RestResult<Object> deleteBatch(Long[] ids){
        ${serviceImplPropertyName}.deleteByIds(ids);
        return messageResult("删除成功！", null);
    }
}