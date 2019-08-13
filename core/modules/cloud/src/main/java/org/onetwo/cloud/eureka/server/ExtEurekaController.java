package org.onetwo.cloud.eureka.server;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.data.AbstractDataResult.StringDataResult;
import org.onetwo.common.spring.rest.ExtRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.server.EurekaController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.eureka.EurekaServerContext;
import com.netflix.eureka.EurekaServerContextHolder;

/**
 * @author weishao zeng
 * <br/>
 */
@Controller
@RequestMapping("${eureka.dashboard.path:/}")
public class ExtEurekaController extends EurekaController {

	@Value("${eureka.dashboard.path:/}")
	private String dashboardPath = "";
	
	private ExtRestTemplate restTemplate = new ExtRestTemplate();
	
	public ExtEurekaController(ApplicationInfoManager applicationInfoManager) {
		super(applicationInfoManager);
	}

	@RequestMapping(path="admin", method = RequestMethod.GET)
	public String admin(HttpServletRequest request, Map<String, Object> model) {
		super.status(request, model);
		return "eureka/admin";
	}

	@RequestMapping(path="remove", method = RequestMethod.POST)
	public String remove(@RequestParam String instId, Map<String, Object> model) {
		List<Application> sortedApplications = getEurekaServerContext().getRegistry().getSortedApplications();
		Optional<InstanceInfo> inst = sortedApplications.stream().filter(app -> {
			return app.getByInstanceId(instId)!=null;
		})
		.findFirst()
		.map(app -> app.getByInstanceId(instId));
		
		if (!inst.isPresent()) {
			model.put("message", "找不到实例：" + instId);
			return "cloud/error";
		}
		
		String url = buildRemoveUrl(inst.get());
		StringDataResult result = this.restTemplate.post(url, null, MediaType.APPLICATION_JSON_UTF8, StringDataResult.class);
		if (result.isError()) {
			model.put("message", "移除实例错误：" + result.getMessage());
			return "cloud/error";
		}
		
		return "redirect:" + this.dashboardPath + "admin";
	}
	
	private String buildRemoveUrl(InstanceInfo inst) {
		String url = "zifishWeb/webmanagement/invoke/eurekaUnregister.json";
		url = inst.getHomePageUrl() + url;
		return url;
	}

	private EurekaServerContext getEurekaServerContext() {
		return EurekaServerContextHolder.getInstance().getServerContext();
	}
}

