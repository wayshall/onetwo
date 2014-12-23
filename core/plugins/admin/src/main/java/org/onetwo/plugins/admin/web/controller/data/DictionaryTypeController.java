package org.onetwo.plugins.admin.web.controller.data;


import javax.validation.Valid;

import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.admin.DataModule.DictModule;
import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity;
import org.onetwo.plugins.admin.model.data.service.DictionaryService;
import org.onetwo.plugins.admin.utils.WebConstant.YesNo;
import org.onetwo.plugins.permission.anno.ByFunctionClass;
import org.onetwo.plugins.permission.anno.ByMenuClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/data/dictionary-type")
@Controller
public class DictionaryTypeController extends PluginSupportedController {
	 
	@Autowired
	private DictionaryService dictionaryService;
	

	@ByMenuClass(codeClass=DictModule.List.class)
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<DictionaryEntity> page){
		dictionaryService.findTypePage(page);
		return pluginMv("/data/dictionary-type-index", "page", page);
	}

	@ByFunctionClass(codeClass=DictModule.New.class)
	@RequestMapping(value="new", method=RequestMethod.GET)
	public ModelAndView _new(@ModelAttribute("dictionary") DictionaryEntity dictionary){
		dictionary.setValid(true);
		return pluginMv("/data/dictionary-type-new", "YesNoTypes", YesNo.values());
	}

	@ByFunctionClass(codeClass=DictModule.New.class)
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(@Valid @ModelAttribute("dictionary")DictionaryEntity dictionary, BindingResult bind, RedirectAttributes redirectAttributes) throws BusinessException{
		if(bind.hasErrors()){
			return pluginMv("/data/dictionary-type-new");
		}
		this.dictionaryService.saveType(dictionary);
		return pluginRedirectTo("/data/dictionary-type", "保存成功！");
	}

	@ByFunctionClass(codeClass=DictModule.Edit.class)
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Long id){
		DictionaryEntity dictionary = this.dictionaryService.findById(id);
		return pluginMv("/data/dictionary-type-edit", "dictionary", dictionary, "YesNoTypes", YesNo.values());
	}
	

	@ByFunctionClass(codeClass=DictModule.Edit.class)
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ModelAndView update(@ModelAttribute("dictionary") @Valid DictionaryEntity dictionary, BindingResult binding, RedirectAttributes redirectAttributes){
		if(binding.hasErrors()){
			return pluginMv("/data/dictionary-type-edit");
		}
		this.dictionaryService.saveType(dictionary);
		return pluginRedirectTo("/data/dictionary-type/", "保存成功！");
	}
	

	@ByFunctionClass(codeClass=DictModule.Delete.class)
	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView deleteBatch(long[] ids, RedirectAttributes redirectAttributes){
		for(Long id : ids){
			this.dictionaryService.removeById(id);
		}
		return pluginRedirectTo("/data/dictionary-type", "删除成功！");
	}
	
	
}