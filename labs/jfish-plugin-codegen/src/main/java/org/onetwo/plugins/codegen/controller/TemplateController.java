package org.onetwo.plugins.codegen.controller;

import javax.validation.Valid;

import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.codegen.model.entity.TemplateEntity;
import org.onetwo.plugins.codegen.model.service.impl.TemplateServiceImpl;
import org.onetwo.plugins.codegen.page.TemplatePage;
import org.onetwo.plugins.fmtagext.BaseRestController;
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

@RequestMapping("/template")
@Controller
public class TemplateController extends BaseRestController<TemplateEntity> {
	 
	@Autowired
	private TemplateServiceImpl templateServiceImpl;

	private TemplatePage temPage;
	
	
	@Override
	public void initBuild() {
		temPage = new TemplatePage(getEntityPathInfo());
		temPage.build();
	}

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<TemplateEntity> page){
		templateServiceImpl.findPage(page, K.DESC, "lastUpdateTime");
		return model(temPage.getListPage(page));
	}
	
	@RequestMapping(value="new", method=RequestMethod.GET)
	public ModelAndView _new(@ModelAttribute("ctemp") TemplateEntity tempate) throws BusinessException{
		return model(temPage.getNewPage(tempate));
//		return innerView("new2");
	}

	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(String redirectUrl, @Valid @ModelAttribute("ctemp")TemplateEntity tempate, BindingResult bind, RedirectAttributes redirectAttributes) throws BusinessException{
		if(bind.hasErrors()){
			return model(temPage.getNewPage(tempate));
		}
		this.templateServiceImpl.save(tempate);
		this.addFlashMessage(redirectAttributes, "保存成功！");
		return redirectTo(getEntityPathInfo().getListPathInfo().getPath());
	}
	
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Long id){
		TemplateEntity db = this.templateServiceImpl.findById(id);
		return model(temPage.getEditPage(db));
	}
	

	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ModelAndView update(@ModelAttribute("ctemp") @Valid TemplateEntity db, BindingResult binding, RedirectAttributes redirectAttributes){
		if(binding.hasErrors()){
			return model(temPage.getEditPage(db));
		}
		this.templateServiceImpl.save(db);
		this.addFlashMessage(redirectAttributes, "保存成功！");
		return model(temPage.getShowPage(db));
	}
	

	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView deleteBatch(@RequestParam(value="ids")long[] ids, RedirectAttributes redirectAttributes){
		for(long id : ids){
			this.templateServiceImpl.removeById(id);
		}
		this.addFlashMessage(redirectAttributes, "删除成功！");
		return redirectTo(getEntityPathInfo().getListPathInfo());
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") long id) throws BusinessException{
		TemplateEntity db =  this.templateServiceImpl.findById(id);
		return model(temPage.getShowPage(db));
	}
}
