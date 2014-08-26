package org.onetwo.plugins.task.client.web;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.permission.anno.ByFunctionClass;
import org.onetwo.plugins.permission.anno.ByMenuClass;
import org.onetwo.plugins.task.client.TaskModule;
import org.onetwo.plugins.task.client.service.impl.TaskClientServiceImpl;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.service.impl.TaskQueueServiceImpl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/taskqueue")
public class TaskQueueController extends PluginSupportedController {
	
	@Resource
	private TaskQueueServiceImpl taskQueueService;
	
	@Resource
	private TaskClientServiceImpl taskClientService;
	
	@ByMenuClass(codeClass=TaskModule.QueueList.class)
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<TaskQueue> page){
		taskQueueService.findPage(page);
		return pluginMv("task-queue-index", "page", page);
	}
	

	@ByFunctionClass(codeClass=TaskModule.New.class)
	@RequestMapping(value="new", method=RequestMethod.GET)
	public ModelAndView _new(TaskQueue taskQueue){
		return pluginMv("task-queue-new");
	}
	
	@ByFunctionClass(codeClass=TaskModule.New.class)
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(@Valid TaskQueue taskQueue, BindingResult bind) throws BusinessException{
		if(bind.hasErrors()){
			return pluginMv("task-queue-new");
		}
		taskClientService.save(taskQueue);
		return pluginRedirectTo("/taskqueue", "保存成功！");
	}
	
	@ByFunctionClass(codeClass=TaskModule.Edit.class)
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Long id){
		TaskQueue taskQueue = taskClientService.load(id);
		return pluginMv("task-queue-edit", "taskQueue", taskQueue);
	}
	

	@ByFunctionClass(codeClass=TaskModule.Edit.class)
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ModelAndView update(@Valid TaskQueue taskQueue, BindingResult binding){
		if(binding.hasErrors()){
			return pluginMv("task-queue-edit");
		}
		taskClientService.save(taskQueue);
		return redirectTo("/taskqueue/"+taskQueue.getId()+"/edit", "保存成功！");
	}
}
