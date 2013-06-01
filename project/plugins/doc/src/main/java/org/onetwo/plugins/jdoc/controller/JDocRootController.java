package org.onetwo.plugins.jdoc.controller;

import java.util.Collection;

import org.onetwo.common.fish.plugin.anno.PluginControllerConf;
import org.onetwo.plugins.jdoc.data.JClassDoc;
import org.onetwo.plugins.jdoc.data.JMethodDoc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@PluginControllerConf(contextPath="/")
@Controller
public class JDocRootController extends JDocBaseController{
	
	
	@RequestMapping(value="/{side}/{restName}/doc", method=RequestMethod.GET)
	public ModelAndView doc(@PathVariable("side") String sideName, @PathVariable("restName") String restName){
		String path = "/" + sideName + "/" +restName;
//		jdocManager.startScanDoc();
		JMethodDoc methodDoc = getJdocManager().findMethodDocByKey(path);
		return mv("show-method", "methodDoc", methodDoc);
	}

	@ModelAttribute("classDocs")
	public Collection<JClassDoc> getClassDocs(){
		return getJdocManager().getClassDocs();
	}

}
