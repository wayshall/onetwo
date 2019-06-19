package org.onetwo.boot.webmgr;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.service.impl.SettingsManager;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.spring.mvc.utils.DataResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wayshall
 * <br/>
 */
@RequestMapping(BootWebUtils.CONTROLLER_PREFIX)
public class SettingsController extends AbstractBaseController {
	
	@Autowired
	private SettingsManager settingsManager;
	
	@PutMapping("/setting/{configName}")
	@ResponseBody
	public Object update(@PathVariable("configName") String configName, @RequestParam("value") String value){
		settingsManager.updateConfig(configName, value);
		return DataResults.success("change config["+configName+"] to :"+value).build();
	}
	
	@PutMapping("/setting/{configName}/reset")
	@ResponseBody
	public Object reset(@PathVariable("configName") String configName){
		String value = settingsManager.reset(configName);
		return DataResults.success("change config["+configName+"] to :"+value).build();
	}
	
	@PutMapping("/settings/reset")
	@ResponseBody
	public Object reset(){
		settingsManager.reset();
		return DataResults.success("reset settings success!").build();
	}


}
