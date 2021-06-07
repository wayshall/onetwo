package org.onetwo.common.spring.ftl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.date.DateInterval;
import org.onetwo.common.date.DateInterval.NiceDateIntervalList;
import org.onetwo.common.date.DateUtils.DateType;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/*****
 * 
 *	以天（date）为间隔，遍历输出从10月1日到11日（不包含）的日期，日期按照format格式化为字符串，format参数不写，则dateVar为Date类型对象
 * [@dateRange from='2014-10-01' to='2014-10-11' type='date' format='yyyyMMdd' joiner=' or '; dateVar, index]
    	t.date = '${dateVar}'
   [/@dateRange]
    	
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
	public static final String PARAMS_FORMAT = "format";
	public static final String PARAMS_JOINER = "joiner";

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		TemplateModel from = FtlUtils.getRequiredParameter(params, PARAMS_FROM);
		TemplateModel to = FtlUtils.getRequiredParameter(params, PARAMS_TO);
		String joiner = FtlUtils.getParameterByString(params, PARAMS_JOINER, "");
		String type = FtlUtils.getParameterByString(params, PARAMS_TYPE, DateType.date.toString());
		boolean includeEnd = FtlUtils.getParameterByBoolean(params, PARAMS_INCLUDE_END, false);
		String format = FtlUtils.getParameterByString(params, PARAMS_FORMAT, null);

		List<?> listDatas = null;
		DateType datetype = DateType.valueOf(type);
		String fromStr = from.toString();
		String toStr = to.toString();
		DateInterval interval = DateInterval.in(fromStr, toStr);
		listDatas = interval.getIntervalNiceDates(datetype, 1, includeEnd);
		
		// 若有格式化参数，则格式化为字符串
		if (StringUtils.isNotBlank(format)) {
			listDatas = ((NiceDateIntervalList)listDatas).format(format);
		}
		
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
