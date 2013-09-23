package org.onetwo.plugins.codegen.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.fish.plugin.PluginBaseController;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.codegen.generator.DefaultTableManager;
import org.onetwo.plugins.codegen.generator.DefaultTableManagerFactory;
import org.onetwo.plugins.codegen.model.entity.DatabaseEntity;
import org.onetwo.plugins.codegen.model.service.impl.DatabaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/database")
@Controller
public class DatabaseController extends PluginBaseController {
	 
	@Autowired
	private DatabaseServiceImpl databaseServiceImpl;
	
	@Autowired
	private DefaultTableManagerFactory tableManagerFactory;

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<DatabaseEntity> page, HttpServletRequest request){
		if(!tableManagerFactory.getDefaultTableManager().getTables().contains(DatabaseEntity.TABLE_NAME)){
			List<DatabaseEntity> list = LangUtils.newArrayList(1);
			list.add(tableManagerFactory.getDefaultDataBase());
			page.setResult(list);
			page.setTotalCount(1);
		}else{
			databaseServiceImpl.findPage(page, K.DESC, "lastUpdateTime");
		}
		return pluginMv("database", "page", page);
	}
	

	@RequestMapping(value="/{id}/tables", method=RequestMethod.GET)
	public ModelAndView tables(@PathVariable("id") long id) throws BusinessException{
//		this.initBuild();
		DefaultTableManager tm = null;
		if(id==tableManagerFactory.getDefaultDataBase().getId()){
			tm = this.tableManagerFactory.getDefaultTableManager();
		}else{
			DatabaseEntity db = this.databaseServiceImpl.load(id);
			tm = this.tableManagerFactory.createTableManager(db);
		}
		List<String> tables = tm.getTableNames(true);
//		return innerView("tables", "tables", tables);
		return pluginMv("database-tables", "tables", tables);
	}
	
}
