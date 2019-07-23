package org.onetwo.boot.webmgr;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author weishao zeng
 * <br/>
 */
@RequestMapping(BootWebUtils.CONTROLLER_PREFIX+"webmanagement")
//@RooUserAuth
public class WebManagementController extends AbstractBaseController {
	
	@Autowired(required=false)
	private WebManagementDelegater webManagementDelegater;
	
	@ResponseBody
	@PostMapping("refreshConfig")
	public Object refreshConfig() {
		// org.springframework.cloud.endpoint.RefreshEndpoint
		return webManagementDelegater.invoke("refreshConfig");
	}

}
