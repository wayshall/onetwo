package org.onetwo.cloud.eureka.server;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.data.AbstractDataResult.StringDataResult;
import org.onetwo.common.spring.rest.ExtRestTemplate;
import org.onetwo.common.spring.rest.RestUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.server.EurekaController;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.shared.Application;
import com.netflix.eureka.EurekaServerContext;
import com.netflix.eureka.EurekaServerContextHolder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author weishao zeng
 * <br/>
 */
@Slf4j
@Controller
@RequestMapping("${eureka.dashboard.path:/}")
public class ExtEurekaController extends EurekaController {

	@Autowired
	private HttpServletRequest request;
	/*@Value("${eureka.dashboard.path:/}")
	private String dashboardPath = "";*/
	@Value("${eureka.dashboard.baseurl:/gateway/eureka}")
	private String baseurl = "";
	@Value("${eureka.dashboard.authHeaderName:auth}")
	private String authHeaderName = "";
	
	private ExtRestTemplate restTemplate = new ExtRestTemplate();
	
	public ExtEurekaController(ApplicationInfoManager applicationInfoManager) {
		super(applicationInfoManager);
	}

	@RequestMapping(path="admin", method = RequestMethod.GET)
	public String admin(HttpServletRequest request, Map<String, Object> model) {
		super.status(request, model);
		return "eureka/admin";
	}

	@RequestMapping(path="instance", method = RequestMethod.POST)
	public String instance(@RequestParam String instId, InstanceStatus status, Map<String, Object> model) {
		Optional<InstanceInfo> inst = findInstanceInfoById(instId);
		
		if (!inst.isPresent()) {
			model.put("message", "找不到实例：" + instId);
			return "cloud/message";
		}
		
		/*
		String url = buildRemoveUrl(inst.get());
		StringDataResult result = this.restTemplate.post(url, null, MediaType.APPLICATION_JSON_UTF8, StringDataResult.class);
		if (result.isError()) {
			model.put("message", "移除实例错误：" + result.getMessage());
			return "cloud/error";
		}*/
		
		inst.get().setStatus(status);
		model.put("message", "实例["+instId+"]上线成功！");
		
		return admin(request, model);
	}


	@RequestMapping(path="refreshConfig", method = RequestMethod.POST)
	public String refreshConfig(@RequestParam String instId, Map<String, Object> model) {
		Optional<InstanceInfo> inst = findInstanceInfoById(instId);
		
		if (!inst.isPresent()) {
			model.put("message", "找不到实例：" + instId);
			return "cloud/message";
		}
		String result = this.refreshInstanceConfig(inst.get());
		model.put("message", "刷新结果：" + result);
		return "cloud/message";
	}

	protected String refreshInstanceConfig(InstanceInfo inst) {
		String url = inst.getMetadata().get("refresh-config-url");
		if (StringUtils.isBlank(url)) {
			url = inst.getMetadata().get("management.context-path");
			if (url==null) {
//				throw new ServiceException("the config [metadata-map.management.context-path] of client is missing!");
				url = "";
			}
			url = inst.getHomePageUrl() + url + "/refresh";
		}
		
		String auth = RequestUtils.getCookieValue(request, authHeaderName);
		HttpHeaders headers = RestUtils.createHeader(MediaType.APPLICATION_JSON_UTF8);
		if (StringUtils.isNotBlank(auth)) {
//			headers.set("auth", auth);
			headers.set("Cookie", authHeaderName+"="+auth);
		}

		if (log.isInfoEnabled()) {
			log.info("refresh config post url: {}, auth: {}", url, auth);
		}
		
		HttpEntity<?> entity = new HttpEntity<>(null, headers);
		String result = this.restTemplate.postForEntity(url, entity, String.class).getBody();
		return result;
	}
	
	protected String removeUnregisterEureka(InstanceInfo inst, Map<String, Object> model) {
		String url = "zifishWeb/webmanagement/invoke/eurekaUnregister.json";
		url = inst.getHomePageUrl() + url;
		StringDataResult result = this.restTemplate.post(url, null, MediaType.APPLICATION_JSON_UTF8, StringDataResult.class);
		if (result.isError()) {
			model.put("message", "移除实例错误：" + result.getMessage());
			return "cloud/message";
		}
		return null;
	}
	
	private Optional<InstanceInfo> findInstanceInfoById(String instId) {
		List<Application> sortedApplications = getEurekaServerContext().getRegistry().getSortedApplications();
		Optional<InstanceInfo> inst = sortedApplications.stream().filter(app -> {
			return app.getByInstanceId(instId)!=null;
		})
		.findFirst()
		.map(app -> app.getByInstanceId(instId));
		return inst;
	}
	
	private EurekaServerContext getEurekaServerContext() {
		return EurekaServerContextHolder.getInstance().getServerContext();
	}
	
	@ModelAttribute("dashboardBaseUrl")
	public String getDashboardBaseurl(){
		return baseurl;
	}
}

