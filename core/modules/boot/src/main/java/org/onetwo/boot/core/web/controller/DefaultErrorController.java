package org.onetwo.boot.core.web.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

public class DefaultErrorController extends PluginBaseController implements ErrorController {
	private static final String PATH = "/error";
	
	@RequestMapping(value = PATH)
    public ModelAndView error() {
        return pluginMv("error");
    }
	 
	@Override
    public String getErrorPath() {
	    return PATH;
    }

}
