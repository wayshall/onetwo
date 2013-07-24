package org.onetwo.plugins.codegen.model.service;

import org.onetwo.common.utils.Page;
import org.onetwo.plugins.codegen.model.entity.TemplateEntity;
import org.springframework.transaction.annotation.Transactional;

public interface TemplateService {

//	public String getTemplateContent(String name);
	
	public TemplateEntity findTempateById(Long id);

	public void findTemplatePage(Page<TemplateEntity> page);

	@Transactional
	public void initCodegenTemplate();

	public TemplateEntity save(TemplateEntity temp);
	
	public TemplateEntity removeById(Long id);
	
	public boolean isInitTemplate();
}