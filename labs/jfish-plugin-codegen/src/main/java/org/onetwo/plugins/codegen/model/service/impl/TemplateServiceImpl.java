package org.onetwo.plugins.codegen.model.service.impl;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.fish.JFishCrudServiceImpl;
import org.onetwo.plugins.codegen.generator.StringTemplateProvider;
import org.onetwo.plugins.codegen.model.entity.TemplateEntity;
import org.springframework.stereotype.Service;


@Service
public class TemplateServiceImpl extends JFishCrudServiceImpl<TemplateEntity, Long> implements StringTemplateProvider {

	@Override
	public String getTemplateContent(String name) {
		TemplateEntity temp = null;
		try {
			Long id = Long.parseLong(name);
			temp = load(id);
		} catch (NumberFormatException e) {
			temp = findUnique("name", name);
		}
		if(temp==null)
			throw new ServiceException("找不到模板：" + name);
		return temp.getContent();
	}

}
