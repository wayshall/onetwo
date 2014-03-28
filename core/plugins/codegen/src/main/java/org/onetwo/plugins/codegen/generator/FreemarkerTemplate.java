package org.onetwo.plugins.codegen.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.annotation.Resource;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.ftl.DynamicFreemarkerTemplateConfigurer;
import org.onetwo.common.spring.ftl.StringTemplateProvider;
import org.onetwo.common.utils.FileUtils;
import org.springframework.beans.factory.InitializingBean;

import freemarker.template.Template;

@SuppressWarnings("unchecked")
public class FreemarkerTemplate extends DynamicFreemarkerTemplateConfigurer implements InitializingBean {
	
	public FreemarkerTemplate(){
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.initialize();
	}

	public File generateFile(TemplateContext context){
		if(context==null){
			throw new BaseException("context can not be null, igonre generated file.");
		}
		BufferedWriter out = null;
		File outfile = null;
		try {
			
			Template template = getConfiguration().getTemplate(context.getTemplate());
			FileUtils.makeDirs(new File(context.getOutfile()).getParent());
			
			outfile = new File(context.getOutfile());
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile), this.getEncoding()));
			context.getContext().put("utils", GenerateUtils.getInstance());
			template.process(context, out);
			System.out.println("file is generate : " + context.getOutfile());
			
		}catch(Exception e){
			throw new BaseException("generate file error : " + context.getOutfile(), e);
		} finally{
			FileUtils.close(out);
		}
		return outfile;
	}
	
	@Resource
	public void setTemplateProvider(StringTemplateProvider templateProvider) {
		super.setTemplateProvider(templateProvider);
	}
}
