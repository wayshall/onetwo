package org.onetwo.plugins.task.webclient.web.taskqueue;

import javax.annotation.Resource;

import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.permission.anno.ByFunctionClass;
import org.onetwo.plugins.permission.anno.ByMenuClass;
import org.onetwo.plugins.task.client.TaskClientConfig;
import org.onetwo.plugins.task.client.service.impl.TaskClientServiceImpl;
import org.onetwo.plugins.task.entity.TaskExecLog;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.webclient.TaskModule.Queue.Cancel;
import org.onetwo.plugins.task.webclient.TaskModule.Queue.ExeLog;
import org.onetwo.plugins.task.webclient.TaskModule.Queue.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/taskqueue")
public class TaskQueueController extends PluginSupportedController {


	@Resource
	private TaskClientServiceImpl taskClientService;

	@Resource
	private TaskClientConfig taskClientConfig;

	@ByMenuClass(codeClass = List.class)
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(Page<TaskQueue> page) {
		taskClientService.findPage(page);
		return pluginMv("task-queue-index", "page", page);
	}

	@ByFunctionClass(codeClass = ExeLog.class)
	@RequestMapping(value = "/{taskQueueId}/log", method = RequestMethod.GET)
	public ModelAndView log(Page<TaskExecLog> page,
			@PathVariable("taskQueueId") Long taskQueueId) {
		taskClientService.findExeLogPage(page, taskQueueId);
		return pluginMv("task-queue-log-list", "page", page);
	}

	@ByFunctionClass(codeClass = Cancel.class)
	@RequestMapping(value = "/{taskQueueId}/cancel", method = RequestMethod.PUT)
	public ModelAndView cancel(Page<TaskExecLog> page,
			@PathVariable("taskQueueId") Long taskQueueId) {
		taskClientService.cancel(taskQueueId);
		return pluginRedirectTo("/taskqueue", "操作成功！");
	}

}
