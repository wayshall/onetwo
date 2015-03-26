package org.onetwo.plugins.task.webclient.web.taskqueue;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.plugins.permission.anno.ByFunctionClass;
import org.onetwo.plugins.task.client.TaskClientConfig;
import org.onetwo.plugins.task.client.service.impl.TaskClientServiceImpl;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.utils.TaskConstant.YesNo;
import org.onetwo.plugins.task.vo.TaskEmailVo;
import org.onetwo.plugins.task.webclient.TaskModule.Queue.Edit;
import org.onetwo.plugins.task.webclient.TaskModule.Queue.New;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/taskqueue/email")
public class EmailTaskController extends PluginSupportedController {


	@Resource
	private TaskClientServiceImpl taskClientService;

	@Resource
	private TaskClientConfig taskClientConfig;

	@ByFunctionClass(codeClass = New.class)
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView emailNew(TaskEmailVo taskQueue) {
		ModelAndView mv = pluginMv("task-queue-email-new", "taskQueue",
				taskQueue);
		mv.addObject("htmlSelector", YesNo.values());
		return mv;
	}

	@ByFunctionClass(codeClass=New.class)
	@RequestMapping(value="",method=RequestMethod.POST)
	public ModelAndView emailCreate(MultipartFile attachment, @Valid @ModelAttribute("taskQueue") TaskEmailVo taskQueue, BindingResult bind){
		if(bind.hasErrors()){
			return pluginMv("task-queue-email-new");
		}
		if(attachment!=null){
			String fn = FileUtils.newFileNameByDateAndRand(attachment.getOriginalFilename());
			String subPath = taskClientConfig.getEmailAttachmentDir() + "web/" + fn;
			JFishWebUtils.writeInputStreamTo(attachment, taskClientConfig.getEmailAttachmentDir(), subPath);
			taskQueue.addAttachment(subPath);
		}
		taskClientService.save(taskQueue);
		return pluginRedirectTo("/taskqueue", "保存成功！");
	}

	@ByFunctionClass(codeClass = Edit.class)
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public ModelAndView emailEdit(@PathVariable("id") Long id) {
		TaskQueue taskQueue = taskClientService.load(id);
		return pluginMv("task-queue-email-edit", "taskQueue", taskQueue);
	}

	@ByFunctionClass(codeClass = Edit.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ModelAndView emailUpdate(
			@Valid @ModelAttribute("taskQueue") TaskEmailVo taskQueue,
			BindingResult binding) {
		if (binding.hasErrors()) {
			return pluginMv("task-queue-email-edit");
		}
		taskClientService.save(taskQueue);
		return redirectTo("/taskqueue/email/" + taskQueue.getId() + "/edit",
				"保存成功！");
	}
}
