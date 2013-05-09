package org.onetwo.plugins.codegen.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.codegen.generator.DefaultTableManager;
import org.onetwo.plugins.codegen.generator.DefaultTableManagerFactory;
import org.onetwo.plugins.codegen.model.entity.DatabaseEntity;
import org.onetwo.plugins.codegen.model.service.impl.DatabaseServiceImpl;
import org.onetwo.plugins.codegen.page.DatabasePage;
import org.onetwo.plugins.fmtagext.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/database")
@Controller
public class DatabaseController extends BaseRestController<DatabaseEntity> {
	 
	@Autowired
	private DatabaseServiceImpl databaseServiceImpl;
	
	@Autowired
	private DefaultTableManagerFactory tableManagerFactory;
	
	private DatabasePage dbpage;
	
	@Override
	public void initBuild() {
		dbpage = new DatabasePage(getEntityPathInfo());
		dbpage.build();
	}

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<DatabaseEntity> page, HttpServletRequest request){
		databaseServiceImpl.findPage(page, K.DESC, "lastUpdateTime");
		return model(dbpage.getListPage(page));
	}
	
	@RequestMapping(value="new", method=RequestMethod.GET)
	public ModelAndView _new(@ModelAttribute("db") DatabaseEntity database) throws BusinessException{
		return model(dbpage.getNewPage(database));
	}

	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(String redirectUrl, @Valid @ModelAttribute("db")DatabaseEntity db, BindingResult bind, RedirectAttributes redirectAttributes) throws BusinessException{
		if(bind.hasErrors()){
			return model(dbpage.getNewPage(db));
		}
		this.databaseServiceImpl.save(db);
		this.addFlashMessage(redirectAttributes, "保存成功！");
		return model(dbpage.getShowPage(db));
	}
	
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Long id){
		DatabaseEntity db = this.databaseServiceImpl.findById(id);
		return model(dbpage.getEditPage(db));
	}
	

	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ModelAndView update(@ModelAttribute("db") @Valid DatabaseEntity db, BindingResult binding, RedirectAttributes redirectAttributes){
		if(binding.hasErrors()){
			return model(dbpage.getEditPage(db));
		}
		this.databaseServiceImpl.save(db);
		this.addFlashMessage(redirectAttributes, "保存成功！");
		return model(dbpage.getShowPage(db));
	}
	

	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView deleteBatch(@RequestParam(value="ids")long[] ids, RedirectAttributes redirectAttributes){
		for(long id : ids){
			this.databaseServiceImpl.removeById(id);
		}
		this.addFlashMessage(redirectAttributes, "删除成功！");
		return redirectTo(getEntityPathInfo().getListPathInfo().getPath());
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") long id) throws BusinessException{
		DatabaseEntity db =  this.databaseServiceImpl.findById(id);
		return model(dbpage.getShowPage(db));
	}
	

	@RequestMapping(value="/{id}/tables", method=RequestMethod.GET)
	public ModelAndView tables(@PathVariable("id") long id) throws BusinessException{
//		this.initBuild();
		DatabaseEntity db = this.databaseServiceImpl.load(id);
		DefaultTableManager tm = this.tableManagerFactory.createTableManager(db);
		List<String> tables = tm.getTableNames(true);
//		return innerView("tables", "tables", tables);
		return model(dbpage.getShowTablesGridPage(tables));
	}
	
}
