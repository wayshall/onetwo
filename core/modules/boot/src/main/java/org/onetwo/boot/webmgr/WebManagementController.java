package org.onetwo.boot.webmgr;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.boot.core.web.view.XResponseView;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author weishao zeng
 * <br/>
 */
@RequestMapping(BootWebUtils.CONTROLLER_PREFIX+"webmanagement")
//@RooUserAuth
@XResponseView
public class WebManagementController extends AbstractBaseController implements InitializingBean {
	
	@Autowired(required=false)
	private List<WebManagementCommand> commands;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("commands: {}", commands);
	}


	@ResponseBody
	@PostMapping("invoke/{commandName}")
	public Object invoke(@PathVariable String commandName, @RequestBody(required=false) Map<String, Object> data) {
		Optional<WebManagementCommand> command = findCommand(commandName);
		if (!command.isPresent()) {
			throw new ServiceException("command not found: " + commandName);
		}
		return command.get().invoke(data);
	}
	
	private Optional<WebManagementCommand> findCommand(String name) {
		if (StringUtils.isBlank(name)) {
			return Optional.empty();
		}
		return this.commands.stream().filter(cmd -> cmd.getName().equals(name)).findFirst();
	}

	
}
