package org.onetwo.plugins.jdoc.controller;

import java.util.Collection;

import org.onetwo.plugins.jdoc.data.JClassDoc;
import org.onetwo.plugins.jdoc.data.JMethodDoc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class JDocController extends JDocBaseController{
	


	@RequestMapping(value={"method"}, method=RequestMethod.GET)
	public ModelAndView showMethod(String name){
//		jdocManager.startScanDoc();
		JMethodDoc methodDoc = this.getJdocManager().findMethodDocByName(name);
		return mv("show-method", "methodDoc", methodDoc, "name", name);
	}
	@RequestMapping(value={"class"}, method=RequestMethod.GET)
	public ModelAndView showClass(String name){
		JClassDoc classDoc = this.getJdocManager().findClassDocByName(name);
		return mv("show-class", "classDoc", classDoc, "name", name);
	}
	@RequestMapping(value={""}, method=RequestMethod.GET)
	public ModelAndView index(){
		return mv("index");
	}
	
	@ModelAttribute("classDocs")
	public Collection<JClassDoc> getClassDocs(){
		return getJdocManager().getClassDocs();
	}

}
