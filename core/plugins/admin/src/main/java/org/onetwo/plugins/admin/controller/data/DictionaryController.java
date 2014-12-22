package org.onetwo.plugins.admin.controller.data;


import javax.validation.Valid;

import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity;
import org.onetwo.plugins.admin.model.data.service.DictionaryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/data/dictionary")
@Controller
public class DictionaryController extends PluginSupportedController {
	 
	@Autowired
	private DictionaryServiceImpl dictionaryServiceImpl;
	

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<DictionaryEntity> page){
		dictionaryServiceImpl.findPage(page);
		return pluginMv("/data/dictionary-index", "page", page);
	}
	
	@RequestMapping(value="new", method=RequestMethod.GET)
	public ModelAndView _new(@ModelAttribute("dictionary") DictionaryEntity dictionary){
		return pluginMv("/data/dictionary-new");
	}

	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(@Valid @ModelAttribute("dictionary")DictionaryEntity dictionary, BindingResult bind, RedirectAttributes redirectAttributes) throws BusinessException{
		if(bind.hasErrors()){
			return pluginMv("/data/dictionary-new");
		}
		this.dictionaryServiceImpl.save(dictionary);
		addFlashMessage(redirectAttributes, "保存成功！");
		return redirectTo("/data/dictionary");
	}
	
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Long id){
		DictionaryEntity dictionary = this.dictionaryServiceImpl.findById(id);
		return pluginMv("/data/dictionary-edit", "dictionary", dictionary);
	}
	

	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ModelAndView update(@ModelAttribute("dictionary") @Valid DictionaryEntity dictionary, BindingResult binding, RedirectAttributes redirectAttributes){
		if(binding.hasErrors()){
			return pluginMv("/data/dictionary-edit");
		}
		this.dictionaryServiceImpl.save(dictionary);
		addFlashMessage(redirectAttributes, "保存成功！");
		return redirectTo("/data/dictionary/"+dictionary.getId());
	}
	

	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView deleteBatch(long[] ids, RedirectAttributes redirectAttributes){
		for(Long id : ids){
			this.dictionaryServiceImpl.removeById(id);
		}
		addFlashMessage(redirectAttributes, "删除成功！");
		return redirectTo("/data/dictionary");
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") long id){
		DictionaryEntity dictionary =  this.dictionaryServiceImpl.findById(id);
		return pluginMv("/data/dictionary-show", "dictionary", dictionary);
	}
	
	
}