package ${fullPackage};

<#assign entityName="${commonName}Entity"/>
<#assign ename="${commonName_uncapitalize}"/>
<#assign serviceImplName="${commonName}ServiceImpl"/>

<#--assign serviceName="${commonName}Service"/-->
<#assign serviceName="${serviceImplName}"/>

<#assign eserviceName="${serviceName?uncap_first}"/>

<#assign entityPackageName="${basePackage}.model.${moduleName}.entity"/>
<#assign serviceImplPackageName="${basePackage}.model.${moduleName}.service.impl"/>

<#-- assign servicePackageName="${modulePackage}.service"/-->
<#assign servicePackageName="${serviceImplPackageName}"/>

<#assign webPath = "${moduleRequestPath}/${ename?lower_case}"/>
<#assign ftlPath = "${moduleRequestPath}/${commonName_partingLine}"/>

import javax.validation.Valid;

import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ${entityPackageName+"."+entityName};
import ${servicePackageName+"."+serviceName};

@RequestMapping("${webPath}")
@Controller
public class ${commonName}Controller extends AbstractBaseController {
	 
	@Autowired
	private ${serviceName} ${eserviceName};
	

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<${entityName}> page){
		${eserviceName}.findPage(page);
		return mv("${ftlPath}-index", "page", page);
	}
	
	@RequestMapping(value="new", method=RequestMethod.GET)
	public ModelAndView _new(@ModelAttribute("${ename}") ${entityName} ${ename}){
		return mv("${ftlPath}-new");
	}

	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(@Valid @ModelAttribute("${ename}")${entityName} ${ename}, BindingResult bind, RedirectAttributes redirectAttributes) throws BusinessException{
		if(bind.hasErrors()){
			return mv("${ftlPath}-new");
		}
		this.${eserviceName}.save(${ename});
		addFlashMessage(redirectAttributes, "保存成功！");
		return redirectTo("${webPath}");
	}
	
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Long ${table.primaryKey.javaName}){
		${entityName} ${ename} = this.${eserviceName}.findById(${table.primaryKey.javaName});
		return mv("${ftlPath}-edit", "${ename}", ${ename});
	}
	

	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ModelAndView update(@ModelAttribute("${ename}") @Valid ${entityName} ${ename}, BindingResult binding, RedirectAttributes redirectAttributes){
		if(binding.hasErrors()){
			return mv("${ftlPath}-edit");
		}
		this.${eserviceName}.save(${ename});
		addFlashMessage(redirectAttributes, "保存成功！");
		return redirectTo("${webPath}/"+${ename}.${table.primaryKey.readMethodName}());
	}
	

	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView deleteBatch(long[] ${table.primaryKey.javaName}s, RedirectAttributes redirectAttributes){
		for(${table.primaryKey.javaType.simpleName} id : ${table.primaryKey.javaName}s){
			this.${eserviceName}.removeById(id);
		}
		addFlashMessage(redirectAttributes, "删除成功！");
		return redirectTo("${webPath}");
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") long ${table.primaryKey.javaName}){
		${entityName} ${ename} =  this.${eserviceName}.findById(${table.primaryKey.javaName});
		return mv("${ftlPath}-show", "${ename}", ${ename});
	}
	
	
}
