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
	private DictionaryService dictionaryService;
	

	@ByFunctionClass(codeClass=DictModule.List.class)
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<DictionaryEntity> page, DictionaryEntity dict){
		dictionaryService.findDataPage(page, dict);
		return pluginMv("/data/dictionary-index", "page", page, "type", dictionaryService.findById(dict.getTypeId()));
	}

	@ByFunctionClass(codeClass=DictModule.New.class)
	@RequestMapping(value="new", method=RequestMethod.GET)
	public ModelAndView _new(@ModelAttribute("dictionary") DictionaryEntity dictionary){
		DictionaryEntity type = dictionaryService.findById(dictionary.getTypeId());
		return pluginMv("/data/dictionary-new", "type", type, "YesNoTypes", YesNo.values());
	}

	@ByFunctionClass(codeClass=DictModule.New.class)
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(@Valid @ModelAttribute("dictionary")DictionaryEntity dictionary, BindingResult bind, RedirectAttributes redirectAttributes) throws BusinessException{
		if(bind.hasErrors()){
			return pluginMv("/data/dictionary-new");
		}
		this.dictionaryService.saveData(dictionary);
		return pluginRedirectTo("/data/dictionary?typeId="+dictionary.getTypeId(), "保存成功！");
	}

	@ByFunctionClass(codeClass=DictModule.Edit.class)
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Long id){
		DictionaryEntity dictionary = this.dictionaryService.findById(id);
		DictionaryEntity type = dictionaryService.findById(dictionary.getTypeId());
		return pluginMv("/data/dictionary-edit", "dictionary", dictionary, "type", type, "YesNoTypes", YesNo.values());
	}
	

	@ByFunctionClass(codeClass=DictModule.Edit.class)
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ModelAndView update(@ModelAttribute("dictionary") @Valid DictionaryEntity dictionary, BindingResult binding, RedirectAttributes redirectAttributes){
		if(binding.hasErrors()){
			return pluginMv("/data/dictionary-edit");
		}
		this.dictionaryService.saveData(dictionary);
		return pluginRedirectTo("/data/dictionary/"+dictionary.getId()+"/edit", "保存成功！");
	}
	

	@ByFunctionClass(codeClass=DictModule.Delete.class)
	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView deleteBatch(long[] ids, RedirectAttributes redirectAttributes){
		DictionaryEntity dict = null;
		for(Long id : ids){
			dict = this.dictionaryService.removeById(id);
		}
		return pluginRedirectTo("/data/dictionary?typeId="+dict.getTypeId(), "删除成功！");
	}
	
}