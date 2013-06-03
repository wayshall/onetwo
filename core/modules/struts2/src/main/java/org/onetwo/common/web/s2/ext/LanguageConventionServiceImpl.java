package org.onetwo.common.web.s2.ext;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.struts2.convention.ConventionsServiceImpl;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.utils.StrutsUtils;

import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ResultTypeConfig;
import com.opensymphony.xwork2.inject.Inject;

public class LanguageConventionServiceImpl extends ConventionsServiceImpl {
	
	public static final String DEFAULT_LANGUAGE = LocaleUtils.getDefault().getLanguage() + "/";

	/******
	 * 不带语言
	 */
    private String redundanceResultPath;
	private boolean autoLanguage;
	private boolean appendLanguageWhenScan;
    
    @Inject
    public LanguageConventionServiceImpl(@Inject("struts.convention.result.path") String resultPath) {
       super(resultPath);
       if(!resultPath.endsWith("/")){
    	   resultPath += "/";
       }
       this.redundanceResultPath = resultPath;
       this.autoLanguage = SiteConfig.inst().isStrutsAutoLanguage();
    }
    
    public boolean isAppendLanguageWhenScan() {
		return appendLanguageWhenScan;
	}

    @Inject(ExtConstant.EXT_CONVENTIONS_SCAN_LANGUAGE)
	public void setAppendLanguageWhenScan(String appendLanguageWhenScan) {
		this.appendLanguageWhenScan = "true".equals(appendLanguageWhenScan);
	}

	public String determineResultPath(Class<?> actionClass) {
        String localResultPath = super.determineResultPath(actionClass);
    	
    	if(autoLanguage){
    		if(StrutsUtils.getRequest()!=null){
    			localResultPath = "/"+StrutsUtils.getSessionLanguage();
    		}else{
    			if(isAppendLanguageWhenScan())
    				localResultPath = redundanceResultPath + DEFAULT_LANGUAGE;
    		}
    	}

        return localResultPath;
    }

    public Map<String, ResultTypeConfig> getResultTypesByExtension(PackageConfig packageConfig) {
        Map<String, ResultTypeConfig> results = packageConfig.getAllResultTypeConfigs();

        Map<String, ResultTypeConfig> resultsByExtension = new LinkedHashMap<String, ResultTypeConfig>();
        resultsByExtension.put("jsp", results.get("dispatcher"));
        resultsByExtension.put("ftl", results.get("freemarker"));
        resultsByExtension.put("html", results.get("dispatcher"));
        return resultsByExtension;
    }
    
}
