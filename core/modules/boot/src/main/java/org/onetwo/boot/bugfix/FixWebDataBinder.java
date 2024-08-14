package org.onetwo.boot.bugfix;

import org.springframework.web.bind.WebDataBinder;

/****
 * 漏洞见：
 * http://blog.nsfocus.net/cve-2022-22965/
 * https://paper.seebug.org/1877/
 * @author way
 */
public class FixWebDataBinder {
	
    public static void fixWebShellVulnerability(WebDataBinder dataBinder) {
		// 设置不允许绑定以下属性，避免webshell漏洞
        dataBinder.setDisallowedFields("class.*", "*.class.*", "Class.*", "*.Class.*");
    }
}
