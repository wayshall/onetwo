package org.onetwo.plugins.task.client.web;

import javax.annotation.Resource;

import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.permission.anno.ByFunctionClass;
import org.onetwo.plugins.permission.anno.ByMenuClass;
import org.onetwo.plugins.task.client.TaskModule.Archived;
import org.onetwo.plugins.task.entity.TaskQueueArchived;
import org.onetwo.plugins.task.service.impl.TaskQueueServiceImpl;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/taskarchived")
public class TaskArchivedController extends PluginSupportedController {
	
	@Resource
	private TaskQueueServiceImpl taskQueueService;
	
	@ByMenuClass(codeClass=Archived.List.class)
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<TaskQueueArchived> page, TaskQueueArchived archived){
		taskQueueService.findArchivedPage(page, "result", archived.getResult(), K.IF_NULL, IfNull.Ignore);
		return pluginMv("task-archived-index", "page", page);
	}
	
	@ByFunctionClass(codeClass=Archived.ReQueue.class)
	@RequestMapping(value="/requeue/{id}", method=RequestMethod.PUT)
	public ModelAndView requeue(@PathVariable("id") Long id){
		taskQueueService.requeueArchived(id);
		return pluginRedirectTo("/taskqueue/archived", "");
	}
	

}
