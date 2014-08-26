package org.onetwo.plugins.task.client.web;

import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.permission.anno.ByMenuClass;
import org.onetwo.plugins.task.client.TaskModule;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.service.impl.TaskQueueServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/taskqueue")
public class TaskQueueController extends PluginSupportedController {
	
	private TaskQueueServiceImpl taskQueueService;
	
	@ByMenuClass(codeClass=TaskModule.QueueList.class)
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<TaskQueue> page){
		taskQueueService.findPage(page);
		return mv("task-queue-index", "page", page);
	}
}
