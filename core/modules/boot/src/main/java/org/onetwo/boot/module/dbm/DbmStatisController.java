package org.onetwo.boot.module.dbm;

import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.utils.BootWebUtils;
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
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public void html(HttpServletResponse response){
		String text = this.sqlExecutedStatis.toFormatedString("<br/>");
		renderHtml(response, text);
	}

}
