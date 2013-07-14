package ${fullPackage};

<#assign entityName="${commonName}Entity"/>
<#assign ename="${commonName?uncap_first}"/>
<#assign serviceImplName="${commonName}ServiceImpl"/>

<#--assign serviceName="${commonName}Service"/-->
<#assign serviceName="${serviceImplName}"/>

<#assign eserviceName="${serviceName?uncap_first}"/>

<#assign entityPackageName="${basePackage}.model.${moduleName}.entity"/>
<#assign serviceImplPackageName="${basePackage}.model.${moduleName}.service.impl"/>

<#-- assign servicePackageName="${modulePackage}.service"/-->
<#assign servicePackageName="${serviceImplPackageName}"/>

<#assign webPath = "${moduleRequestPath}/${ename}"/>

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
		${eserviceName}.findPage(page, K.DESC, "lastUpdateTime");
		return mv("${webPath}-index", "page", page);
	}
	
	@RequestMapping(value="new", method=RequestMethod.GET)
	public ModelAndView _new(@ModelAttribute("${ename}") ${entityName} ${ename}){
		return mv("${webPath}-new");
	}

	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(String redirectUrl, @Valid @ModelAttribute("${ename}")${entityName} ${ename}, BindingResult bind, RedirectAttributes redirectAttributes) throws BusinessException{
		if(bind.hasErrors()){
			return mv("${webPath}-new");
		}
		this.${eserviceName}.save(${ename});
		addFlashMessage(redirectAttributes, "保存成功！");
		return redirectTo("${webPath}");
	}
	
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Long id){
		${entityName} ${ename} = this.${eserviceName}.findById(id);
		return mv("${webPath}-edit", "${ename}", ${ename});
	}
	

	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ModelAndView update(@ModelAttribute("${ename}") @Valid ${entityName} ${ename}, BindingResult binding, RedirectAttributes redirectAttributes){
		if(binding.hasErrors()){
			return mv("${webPath}-edit");
		}
		this.${eserviceName}.save(${ename});
		addFlashMessage(redirectAttributes, "保存成功！");
		return redirectTo("${webPath}/"+${ename}.getId());
	}
	

	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView deleteBatch(@RequestParam(value="ids")long[] ids, RedirectAttributes redirectAttributes){
		for(long id : ids){
			this.${eserviceName}.removeById(id);
		}
		addFlashMessage(redirectAttributes, "删除成功！");
		return redirectTo("${webPath}");
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") long id) throws BusinessException{
		${entityName} ${ename} =  this.${eserviceName}.findById(id);
		return mv("${webPath}-show", "${ename}", ${ename});
	}
	
	
}
