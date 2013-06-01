package org.onetwo.plugins.codegen.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.spring.web.BaseController;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;
import org.onetwo.plugins.codegen.generator.CommonlContextBuilder;
import org.onetwo.plugins.codegen.generator.DefaultCodegenServiceImpl;
import org.onetwo.plugins.codegen.generator.DefaultTableManager;
import org.onetwo.plugins.codegen.generator.DefaultTableManagerFactory;
import org.onetwo.plugins.codegen.generator.FreemarkerTemplate;
import org.onetwo.plugins.codegen.generator.GenContext;
import org.onetwo.plugins.codegen.model.entity.DatabaseEntity;
import org.onetwo.plugins.codegen.model.entity.TemplateEntity;
import org.onetwo.plugins.codegen.model.service.impl.DatabaseServiceImpl;
import org.onetwo.plugins.codegen.model.service.impl.TemplateServiceImpl;
import org.onetwo.plugins.fmtagext.ui.UI;
import org.onetwo.plugins.fmtagext.ui.aglybuilder.EntityGridUIBuilder;
import org.onetwo.plugins.fmtagext.ui.datagrid.DataGridUI;
import org.onetwo.plugins.fmtagext.ui.form.FormLinkButtonUI;
import org.onetwo.plugins.fmtagext.ui.form.FormTextUI;
import org.onetwo.plugins.fmtagext.ui.form.FormUI;
import org.onetwo.plugins.fmtagext.ui.form.FormUI.FormMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CodegenController extends BaseController<Object>{

	@Autowired
	private TemplateServiceImpl templateServiceImpl;
	
	@Autowired
	private DatabaseServiceImpl databaseServiceImpl;
	
	@Autowired
	private DefaultTableManagerFactory tableManagerFactory;

	@Autowired
	private FreemarkerTemplate freemarkerTemplate;
	
	@RequestMapping(value={"", "/"})
	public String index(){
		return redirect("/codegen/database");
	}

	@RequestMapping(value="/config", method=RequestMethod.POST)
	public ModelAndView config(@ModelAttribute("dbid") Long dbid, String[] tables, Page<TemplateEntity> tp) throws BusinessException{
		System.out.println("tables: " + LangUtils.toString(tables));
		templateServiceImpl.findPage(tp, K.DESC, "lastUpdateTime");
		
		EntityGridUIBuilder listgridBuilder = new EntityGridUIBuilder(TemplateEntity.class);
//		listgridBuilder.buildForm();
		listgridBuilder.buildCheckbox();
		listgridBuilder.buildForEntity();
		FormLinkButtonUI btnGen = new FormLinkButtonUI("", "生成实体");
		btnGen.setDataMethod(FormMethod.post);
		btnGen.setDataConfirm("确定要为这些表生成实体？");
//		listgridBuilder.buildToolbarMenus(btnGen);
		listgridBuilder.buildPagination(false);
		
		FormUI form = new FormUI("");
		form.setFormAction("post", "/codegen/gencode");
		if(!LangUtils.isEmpty(tables)){
			for(String table : tables){
				form.addHidden("tables", table);
			}
		}
		form.addFormField(new FormTextUI("要生成代码的表", StringUtils.join(tables, ",")));
		form.addTextInput("basePackage", "包名");
		form.addTextInput("generateOutDir", "输出目录");
		form.addTextInput("tablePrefix", "要去掉的前缀");
		form.addHidden("dbid", dbid.toString());
		DataGridUI dg = listgridBuilder.buildUIComponent();
		dg.setTitle("选择代码模板：");
		form.getChildren().addChild(dg);
		form.addButtons(UI.submit());
		
//		return mv("gen-config", "tp", tp, "tables", tables);
		return model(UI.page(form, tp));
	}

	@RequestMapping(value="/gencode", method=RequestMethod.POST)
	public ModelAndView gencode(@ModelAttribute("dbid") Long dbid, String[] tables, Long[] ids, GenContext context) throws BusinessException{
		System.out.println("context: " + context);
		if(LangUtils.isEmpty(ids)){
			return mv("gen-result", MESSAGE, "请选择模板！");
		}
		
		DatabaseEntity db = this.databaseServiceImpl.load(dbid);
		DefaultTableManager tm = this.tableManagerFactory.createTableManager(db);
		DefaultCodegenServiceImpl cs = new DefaultCodegenServiceImpl(freemarkerTemplate, tm);
		for(Long id : ids){
			TemplateEntity templ = this.templateServiceImpl.load(id);
			CommonlContextBuilder cmb = new CommonlContextBuilder();
			cmb.setName(templ.getName());
			cmb.setTemplate(id.toString());
			cmb.setOutFileNameCapitalize(true);
			cmb.setFileNamePostfix(templ.getFileNamePostfix());
			cmb.setFilePostfix(templ.getFilePostfix());
			cmb.setSelfPackage(templ.getPackageName());
			cs.addContextBuilder(cmb);
		}
		JFishList<String> tableList = JFishList.wrapObject(tables);
		JFishList<Object> result = cs.generateTables(tableList, context);
		final List<String> messages = new ArrayList<String>();
		result.each(new NoIndexIt<Object>() {

			@Override
			protected void doIt(Object element) throws Exception {
				if(File.class.isInstance(element)){
					messages.add(((File)element).getPath());
				}else{
					messages.add(element.toString());
				}
			}
			
		});
		return mv("gen-result", MESSAGE, "生成成功！", "genMessages", messages);
	}

}
