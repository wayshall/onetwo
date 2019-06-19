package org.onetwo.boot.webmgr;

import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.boot.module.dbm.BootDbmConfig;
import org.onetwo.dbm.stat.SqlExecutedStatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author wayshall
 * <br/>
 */
@RequestMapping(BootWebUtils.CONTROLLER_PREFIX+"/dbm/statis")
public class DbmStatisController extends AbstractBaseController {
	@Autowired
	private SqlExecutedStatis sqlExecutedStatis;
	@Autowired
	private BootDbmConfig bootDbmConfig;
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public void html(HttpServletResponse response){
		if (!bootDbmConfig.isStatisController()) {
			renderHtml(response, "未启用");
			return ;
		}
		String text = this.sqlExecutedStatis.toFormatedString("<br/>");
		renderHtml(response, text);
	}

}
