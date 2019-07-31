package org.onetwo.common.spring.ftl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/*****
 * 
 * [@str insertPrefix='where' trimPrefixs='and | or' trimSuffixs='and | or']
        [#if query.userName?has_content]
            u.user_name = :query.userName
        [/#if]
        [#if query.age??]
            and u.age = :query.age
        [/#if]
        [#if query.status??]
            and u.status = :query.status
        [/#if]
    [/@str]
    	
 * @author way
 *
 */
@SuppressWarnings("rawtypes")
public class StrDirective implements NamedDirective {
	
	public static final String DIRECTIVE_NAME = "str";

	public static final String SPLIT_CHAR = "|";
	public static final String PARAMS_INSERT_PREFIX = "insertPrefix";
	public static final String PARAMS_TRIM_PREFIXS = "trimPrefixs";
	public static final String PARAMS_TRIM_SUFFIXS = "trimSuffixs";

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		String insertPrefix = FtlUtils.getRequiredParameterByString(params, PARAMS_INSERT_PREFIX);
		String trimPrefixs = FtlUtils.getParameterByString(params, PARAMS_TRIM_PREFIXS, "");
		String trimSuffixs = FtlUtils.getParameterByString(params, PARAMS_TRIM_SUFFIXS, "");
		
		StringWriter writer = new StringWriter();
		body.render(writer);
		
		StringBuilder buffer = new StringBuilder(writer.toString().trim());
		if (StringUtils.isBlank(buffer)) {
			return ;
		}

		final String sql = buffer.toString().toLowerCase();
		
		trimPrefixs(buffer, sql, trimPrefixs);
		trimSuffixs(buffer, sql, trimSuffixs);
		
		buffer.insert(0, " ");
		buffer.insert(0, insertPrefix);
		env.getOut().append(buffer);
	}
	
	private void trimPrefixs(StringBuilder buffer, String sql, String trimPrefixs) {
		List<String> trimPrefixList = Arrays.asList(StringUtils.split(trimPrefixs.toLowerCase(), SPLIT_CHAR));
		for(String prefix : trimPrefixList) {
			if (sql.startsWith(prefix)) {
				buffer.delete(0, prefix.length());
				break;
			}
		}
	}
	
	private void trimSuffixs(StringBuilder buffer, String sql, String trimSuffixs) {
		List<String> trimSuffixList = Arrays.asList(StringUtils.split(trimSuffixs.toLowerCase(), SPLIT_CHAR));
		for(String suffix : trimSuffixList) {
			if (sql.endsWith(suffix)) {
				int start = buffer.length()-suffix.length();
				buffer.delete(start, buffer.length());
				break;
			}
		}
	}

	@Override
	public String getName() {
		return DIRECTIVE_NAME;
	}

}
