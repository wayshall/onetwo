package org.onetwo.common.spring.ftl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.DateInterval;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.DateUtil.DateType;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/*****
 * 
 * [@foreach from='2014-10-01' to='2014-10-11' format='yyyyMMdd' joiner=' or '; dpcode, index]
    		-- rtcs.linedept= :deptCode${index}
    		bbl.dptcode = :deptCode${index}
    	[/@foreach]
    	
 * @author way
 *
 */
@SuppressWarnings("rawtypes")
public class DateRangeDirective implements NamedDirective {
	
	public static final String DIRECTIVE_NAME = "dateRange";

	public static final String PARAMS_FROM = "from";
	public static final String PARAMS_TO = "to";
	public static final String PARAMS_TYPE = "type";
	public static final String PARAMS_INCLUDE_END = "includeEnd";
//	public static final String PARAMS_FORMAT = "format";
	public static final String PARAMS_JOINER = "joiner";

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		TemplateModel from = FtlUtils.getRequiredParameter(params, PARAMS_FROM);
		TemplateModel to = FtlUtils.getRequiredParameter(params, PARAMS_TO);
		String joiner = FtlUtils.getParameterByString(params, PARAMS_JOINER, "");
		String type = FtlUtils.getParameterByString(params, PARAMS_TYPE, DateType.date.toString());
//		String format = FtlUtils.getParameterByString(params, PARAMS_FORMAT, DateUtil.Date_Only);
		boolean includeEnd = FtlUtils.getParameterByBoolean(params, PARAMS_INCLUDE_END, false);

		List<?> listDatas = null;
		DateType datetype = DateType.valueOf(type);
		String fromStr = from.toString();
		String toStr = to.toString();
		DateInterval interval = DateInterval.in(fromStr, toStr);
		listDatas = interval.getInterval(datetype, 1, includeEnd);
		
		int index = 0;
		for(Object data : listDatas){
			if(loopVars.length>=1)
				loopVars[0] = FtlUtils.wrapAsModel(data);
			if(loopVars.length>=2)
				loopVars[1] = FtlUtils.wrapAsModel(index);
			
			if(index!=0)
				env.getOut().write(joiner);
			
			body.render(env.getOut());
			index++;
		}
	}

	@Override
	public String getName() {
		return DIRECTIVE_NAME;
	}

}
