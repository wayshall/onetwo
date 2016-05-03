package org.onetwo.common.excel.etemplate.directive;

import java.util.regex.Matcher;

import org.onetwo.common.excel.etemplate.ETSheetContext.ETRowContext;
import org.onetwo.common.excel.etemplate.ExcelTemplateValueProvider;
import org.springframework.util.Assert;

public class IfRowDirective extends AbstractRowDirective<IfRowDirectiveModel> {

	public IfRowDirective() {
		super("if", 
				"^(?i)\\[if\\s+([^\\]]+)\\]$", 
				"^(?i)\\[/if\\s*\\]$");
	}

	@Override
	protected IfRowDirectiveModel createModelByStartMatcher(String tagText, Matcher matcher) {
		String condition = matcher.group(1).trim();
		Assert.hasText(condition);
		IfRowDirectiveModel ifModel = new IfRowDirectiveModel(tagText, condition);
		return ifModel;
	}

	@Override
	protected void excecute(ETRowContext rowContext, IfRowDirectiveModel model, ExcelTemplateValueProvider provider) {
		Boolean res = provider.parseValue(model.getCondition(), Boolean.class);
		if(!res){
			return ;
		}
		
		model.getMatchRows().forEach(row->{
			processCommonRow(row.getRow(), rowContext.getSheetContext().getValueProvider());
		});
	}

}
