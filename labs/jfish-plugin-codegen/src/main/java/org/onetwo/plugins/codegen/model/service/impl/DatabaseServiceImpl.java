package org.onetwo.plugins.codegen.model.service.impl;
import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.fish.JFishCrudServiceImpl;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.codegen.generator.DefaultTableManagerFactory;
import org.onetwo.plugins.codegen.model.dao.DatabaseDao;
import org.onetwo.plugins.codegen.model.entity.DatabaseEntity;
import org.onetwo.plugins.codegen.model.entity.TemplateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DatabaseServiceImpl extends JFishCrudServiceImpl<DatabaseEntity, Long> {
	
	@Resource
	private DatabaseDao databaseDao;
	
	@Autowired
	private DefaultTableManagerFactory tableManagerFactory;
	
	@Transactional
	public void initCodegen(){
		databaseDao.createTemplateTable();
		
		String dir = FileUtils.getResourcePath("META-INF/codegen/template");
		if(StringUtils.isBlank(dir))
			return ;
		File[] files = FileUtils.listFiles(dir, ".ftl");
		if(files==null)
			return ;
		for(File file :files){
			TemplateEntity temp = new TemplateEntity();
			
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
				temp.setName(temp.getFileNamePostfix());
			}else{
				temp.setName(fname);
			}
			List<String> lines = FileUtils.readAsList(file);
			String content = StringUtils.join(lines, String.valueOf('\n'));
			temp.setContent(content);
			
			getBaseEntityManager().save(temp);
		}
		
	}
	
	private String parseName(String name){
		return "-".equals(name)?"":name;
	}
	
	public DatabaseEntity load(Long id) {
		DatabaseEntity db = null;
		if(id==this.tableManagerFactory.getDefaultDataBase().getId()){
			db = this.tableManagerFactory.getDefaultDataBase();
		}else{
			db = super.load(id);
		}
		return db;
	}
}
