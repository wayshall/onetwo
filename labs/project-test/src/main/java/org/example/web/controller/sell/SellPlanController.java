package org.example.web.controller.sell;


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

import org.example.model.sell.entity.SellPlanEntity;
import org.example.model.sell.service.impl.SellPlanServiceImpl;

@RequestMapping("/sell/sellplan")
@Controller
public class SellPlanController extends AbstractBaseController {
	 
	@Autowired
	private SellPlanServiceImpl sellPlanServiceImpl;
	

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<SellPlanEntity> page){
		sellPlanServiceImpl.findPage(page);
		return mv("/sell/sell-plan-index", "page", page);
	}
	
	@RequestMapping(value="new", method=RequestMethod.GET)
	public ModelAndView _new(@ModelAttribute("sellPlan") SellPlanEntity sellPlan){
		return mv("/sell/sell-plan-new");
	}

	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(String redirectUrl, @Valid @ModelAttribute("sellPlan")SellPlanEntity sellPlan, BindingResult bind, RedirectAttributes redirectAttributes) throws BusinessException{
		if(bind.hasErrors()){
			return mv("/sell/sell-plan-new");
		}
		this.sellPlanServiceImpl.save(sellPlan);
		addFlashMessage(redirectAttributes, "保存成功！");
		return redirectTo("/sell/sellplan");
	}
	
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Long id){
		SellPlanEntity sellPlan = this.sellPlanServiceImpl.findById(id);
		return mv("/sell/sell-plan-edit", "sellPlan", sellPlan);
	}
	

	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ModelAndView update(@ModelAttribute("sellPlan") @Valid SellPlanEntity sellPlan, BindingResult binding, RedirectAttributes redirectAttributes){
		if(binding.hasErrors()){
			return mv("/sell/sell-plan-edit");
		}
		this.sellPlanServiceImpl.save(sellPlan);
		addFlashMessage(redirectAttributes, "保存成功！");
		return redirectTo("/sell/sellplan/"+sellPlan.getPlanId());
	}
	

	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView deleteBatch(@RequestParam(value="ids")long[] ids, RedirectAttributes redirectAttributes){
		for(long id : ids){
			this.sellPlanServiceImpl.removeById(id);
		}
		addFlashMessage(redirectAttributes, "删除成功！");
		return redirectTo("/sell/sellplan");
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") long id) throws BusinessException{
		SellPlanEntity sellPlan =  this.sellPlanServiceImpl.findById(id);
		return mv("/sell/sell-plan-show", "sellPlan", sellPlan);
	}
	
	
}