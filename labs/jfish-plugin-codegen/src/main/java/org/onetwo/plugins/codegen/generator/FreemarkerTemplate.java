package org.onetwo.plugins.codegen.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.CharsetUtils;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

@SuppressWarnings("unchecked")
public class FreemarkerTemplate {

	private Configuration cfg;
	
	private String encoding = CharsetUtils.UTF_8;
	
//	private String templateDir;
	
	@Autowired
	private StringTemplateProvider templateProvider;
	
	public FreemarkerTemplate(){
	}

	@PostConstruct
	public void init() {
		Assert.notNull(templateProvider);
		try {
			Map<String, Object> freemarkerVariables = LangUtils.newHashMap();
			
			this.cfg = new Configuration();
			this.cfg.setObjectWrapper(ObjectWrapper.DEFAULT_WRAPPER);
			this.cfg.setOutputEncoding(this.encoding);
//			this.cfg.setDirectoryForTemplateLoading(new File(templateDir));
			this.cfg.setTemplateLoader(new DynamicTemplateLoader(templateProvider));
			cfg.setAllSharedVariables(new SimpleHash(freemarkerVariables, cfg.getObjectWrapper()));
		} catch (Exception e) {
			throw new BaseException("create codegen freemarker template error : " + e.getMessage(), e);
		}
	}

	public File generateFile(TemplateContext context){
		if(context==null){
			throw new BaseException("context can not be null, igonre generated file.");
		}
		BufferedWriter out = null;
		File outfile = null;
		try {
			
			Template template = cfg.getTemplate(context.getTemplate());
			FileUtils.makeDirs(new File(context.getOutfile()).getParent());
			
			outfile = new File(context.getOutfile());
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile), this.encoding));
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

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/*public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}*/

}
