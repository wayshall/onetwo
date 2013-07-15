package org.onetwo.plugins.codegen.model.service.impl;
import org.onetwo.common.fish.JFishCrudServiceImpl;
import org.onetwo.plugins.codegen.generator.DefaultTableManagerFactory;
import org.onetwo.plugins.codegen.model.entity.DatabaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DatabaseServiceImpl extends JFishCrudServiceImpl<DatabaseEntity, Long> {
	
	
	@Autowired
	private DefaultTableManagerFactory tableManagerFactory;
	
	
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
