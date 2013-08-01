package org.onetwo.plugins.codegen.model.service.impl;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.codegen.generator.StringTemplateProvider;
import org.onetwo.plugins.codegen.model.entity.TemplateEntity;
import org.onetwo.plugins.codegen.model.service.TemplateService;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class FileTemplateServiceImpl implements TemplateService, StringTemplateProvider {

	private PathMatchingResourcePatternResolver scaner = new PathMatchingResourcePatternResolver();
	
	final private Class<TemplateEntity> entityClass = TemplateEntity.class;

	@Resource
	private BaseEntityManager baseEntityManager;
	
	private Map<Long, TemplateEntity> templates = LangUtils.newHashMap();
	
	@Override
	public String getTemplateContent(String name) {
		TemplateEntity temp = null;
		try {
			Long id = Long.parseLong(name);
			temp = templates.get(id);
		} catch (NumberFormatException e) {
			temp = baseEntityManager.findUnique(entityClass, "name", name);
		}
		if(temp==null)
			throw new ServiceException("找不到模板：" + name);
		if(StringUtils.isNotBlank(temp.getFilePath())){
			String content = FileUtils.readAsString(temp.getFilePath());
			if(StringUtils.isNotBlank(content))
				return content;
		}
		return temp.getContent();
	}
	
	public void findTemplatePage(Page<TemplateEntity> page){
//		super.findPage(page, K.DESC, "lastUpdateTime");
		page.setResult(new ArrayList<TemplateEntity>(templates.values()));
	}
	@Transactional
	public void initCodegenTemplate(){
		this.templates.clear();
//		String dir = FileUtils.getResourcePath("META-INF/codegen/template");
//		if(StringUtils.isBlank(dir))
//			return ;
		
		org.springframework.core.io.Resource[] ftlList = null;
		
		try {
			ftlList = scaner.getResources("META-INF/codegen/template/*.ftl");
			if(ftlList==null)
				return ;
			
			long index = 1;
			for(org.springframework.core.io.Resource ftlRes : ftlList){
				File file = ftlRes.getFile();
				
				TemplateEntity temp = new TemplateEntity();
				
				temp.setId(index++);
				String fname = FileUtils.getFileNameWithoutExt(file.getName());
				String postfix = FileUtils.getExtendName(fname);
				if(StringUtils.isNotBlank(postfix)){
					fname = FileUtils.getFileNameWithoutExt(fname);
					temp.setFilePostfix(postfix);
				}else{
					temp.setFilePostfix("ftl");
				}
				String[] names = StringUtils.split(fname, "_");
				
				temp.setPackageName(parseName(names[0]));
				if(names.length>=2){
					temp.setFileNamePostfix(parseName(names[1]));
					temp.setName(temp.getPackageName()+"_"+temp.getFileNamePostfix());
				}else{
					temp.setName(temp.getPackageName()+"_"+fname);
				}
				List<String> lines = FileUtils.readAsList(file);
				String content = StringUtils.join(lines, String.valueOf('\n'));
				temp.setContent(content);
				
//				getBaseEntityManager().save(temp);
				templates.put(temp.getId(), temp);
			}
		} catch (Exception e) {
			throw new BaseException("scan template error: " + e.getMessage());
		}
		
		
	}
	
	private String parseName(String name){
		return "-".equals(name)?"":name;
	}

	@Override
	public TemplateEntity findTempateById(Long id) {
		return this.templates.get(id);
	}

	@Override
	public boolean isInitTemplate() {
		return !this.templates.isEmpty();
	}

	@Override
	public TemplateEntity save(TemplateEntity temp) {
		return baseEntityManager.save(temp);
	}

	@Override
	public TemplateEntity removeById(Long id) {
		return baseEntityManager.removeById(entityClass, id);
	}
	
}
