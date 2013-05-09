package org.onetwo.plugins.jdoc.controller;

import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.web.BaseController;
import org.onetwo.plugins.jdoc.Lexer.JavadocManager;
import org.springframework.stereotype.Controller;

@SuppressWarnings("rawtypes")
@Controller
public class JDocBaseController extends BaseController{
	
	private JavadocManager jdocManager;
	
	private void checkJavadocManager(){
		if(jdocManager==null)
			this.jdocManager = SpringApplication.getInstance().getBean(JavadocManager.class);
	}

	public JavadocManager getJdocManager() {
		this.checkJavadocManager();
		return jdocManager;
	}

	public void setJdocManager(JavadocManager jdocManager) {
		this.jdocManager = jdocManager;
	}
}
