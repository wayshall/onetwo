package org.onetwo.plugins.codegen.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.fish.spring.config.JFishAppConfigrator;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;
import org.onetwo.common.web.csrf.CsrfValid;
import org.onetwo.plugins.codegen.generator.CommonlContextBuilder;
import org.onetwo.plugins.codegen.generator.DefaultCodegenServiceImpl;
import org.onetwo.plugins.codegen.generator.DefaultTableManager;
import org.onetwo.plugins.codegen.generator.DefaultTableManagerFactory;
import org.onetwo.plugins.codegen.generator.FreemarkerTemplate;
import org.onetwo.plugins.codegen.generator.GenContext;
import org.onetwo.plugins.codegen.model.entity.DatabaseEntity;
import org.onetwo.plugins.codegen.model.entity.TemplateEntity;
import org.onetwo.plugins.codegen.model.service.CodeTemplateService;
import org.onetwo.plugins.codegen.model.service.impl.DatabaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@CsrfValid(false)
public class CodegenController extends PluginSupportedController {

	@Autowired
	private CodeTemplateService templateService;
	
	@Autowired
	private DatabaseServiceImpl databaseServiceImpl;
	
	@Autowired
	private DefaultTableManagerFactory tableManagerFactory;

	@Autowired
	private FreemarkerTemplate freemarkerTemplate;
	
	@Autowired
	private JFishAppConfigrator JFishAppConfigrator;
	
	public CodegenController(){
	}
	@RequestMapping(value={"", "/"})
	public String index(){
		return redirect("/codegen/database");
	}
	
	@RequestMapping(value="init", method=RequestMethod.POST)
	public ModelAndView init(){
		this.templateService.initCodegenTemplate();
		
		return pluginMv("codegen-init");
	}

	@RequestMapping(value="/config", method=RequestMethod.POST)
	public ModelAndView config(Long dbid, String[] tables, Page<TemplateEntity> tp) throws BusinessException{
		templateService.findTemplatePage(tp);
		
//		EntityGridUIBuilder listgridBuilder = new EntityGridUIBuilder(TemplateEntity.class);
////		listgridBuilder.buildForm();
//		listgridBuilder.buildCheckbox();
//		listgridBuilder.buildForEntity();
//		FormLinkButtonUI btnGen = new FormLinkButtonUI("", "生成实体");
//		btnGen.setDataMethod(FormMethod.post);
//		btnGen.setDataConfirm("确定要为这些表生成实体？");
////		listgridBuilder.buildToolbarMenus(btnGen);
//		listgridBuilder.buildPagination(false);
//		
//		FormUI form = new FormUI("");
//		form.setFormAction("post", "/codegen/gencode");
//		if(!LangUtils.isEmpty(tables)){
//			for(String table : tables){
//				form.addHidden("tables", table);
//			}
//		}
//		form.addFormField(new FormTextUI("要生成代码的表", StringUtils.join(tables, ",")));
//		form.addTextInput("modelName", "模块名");
////		form.addTextInput("generateOutDir", "输出目录");
//		form.addTextInput("tablePrefix", "要去掉的前缀");
//		form.addHidden("dbid", dbid.toString());
//		DataGridUI dg = listgridBuilder.buildUIComponent();
//		dg.setTitle("选择代码模板：");
//		dg.setInitFormName(form.getName());
//		form.getChildren().addChild(dg);
//		form.addButtons(UI.submit());
		
		return pluginMv("gen-config", "page", tp, "tables", tables, "dbid", dbid);
//		return model(UI.page(form, tp));
	}

	@RequestMapping(value="/gencode", method=RequestMethod.POST)
	public ModelAndView gencode(@ModelAttribute("dbid") Long dbid, String[] tables, Long[] ids, GenContext context) throws BusinessException{
		
		System.out.println("context: " + context);
		if(LangUtils.isEmpty(ids)){
			return mv("gen-result", MESSAGE, "请选择模板！");
		}
		
		this.templateService.initCodegenTemplate();
		DatabaseEntity db = this.databaseServiceImpl.load(dbid);
		DefaultTableManager tm = this.tableManagerFactory.createTableManager(db);
		DefaultCodegenServiceImpl cs = new DefaultCodegenServiceImpl(freemarkerTemplate, tm);
		for(Long id : ids){
			TemplateEntity templ = this.templateService.findTempateById(id);
			CommonlContextBuilder cmb = new CommonlContextBuilder(db.getDataBase());

			cmb.setBasePackage(this.JFishAppConfigrator.getJFishBasePackage());
			if(templ.getFileNamePostfix().equalsIgnoreCase("Entity") 
					|| templ.getFileNamePostfix().equalsIgnoreCase("Service")
					|| templ.getFileNamePostfix().equalsIgnoreCase("ServiceImpl")){
				cmb.setArchetypePackage("model");
			}else if(templ.getFileNamePostfix().equalsIgnoreCase("Controller")){
				cmb.setArchetypePackage("web.controller");
			}else{
				cmb.setArchetypePackage("");
			}
			cmb.setModuleName(context.getModelName());
			cmb.setName(templ.getName());
			cmb.setTemplate(id.toString());
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
		return pluginMv("gen-result", MESSAGE, "生成成功！", "genMessages", messages);
	}

}
