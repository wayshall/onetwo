package org.onetwo.common.spring.web;

import org.onetwo.common.db.IdEntity;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.MappedEntryManager;
import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.fish.plugin.JFishPluginMeta;
import org.onetwo.common.fish.plugin.PluginConfig;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.web.mvc.SingleReturnWrapper;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

abstract public class BaseController<E> extends AbstractBaseController implements InitializingBean {
//	public static final String SINGLE_MODEL_FLAG_KEY = "__SINGLE_MODEL_FLAG_KEY__";
	
	public static final Object LAZY_RESULT = new Object();
	
	public static boolean isLazyResult(Object obj){
		if(obj==null)
			return false;
		return BaseController.LAZY_RESULT.equals(obj);
	}
	
	public static class UrlHelper {
		public static final String MODEL_KEY = "urlHelper";
		
		public static final String DEFAULT_OP = "-";
		public static final String SLASH = "/";
//		public static final String CONTROLLER_POSTFIX = "Controller";
//		public static final String CONTROLLER_PACKAGE = ".controller";
//		public static final String ACTION_POSTFIX = ".do";

		public static final String LIST = "list";
		public static final String SHOW = "show";
		public static final String NEW = "new";
		public static final String EDIT = "edit";
		

		public static final String CREATE = "create";
		public static final String UPDATE = "update";
		
//		private static final boolean APPEND_POSTFIX = false;
		
//		private Class<?> controllerClass;
		private String packageUrlMapping;
		private PluginConfig pluginConfig;

		public UrlHelper(Class<?> controllerClass, PluginConfig pluginConfig) {
			this.pluginConfig = pluginConfig;
			RequestMapping rm = controllerClass.getAnnotation(RequestMapping.class);
//			Assert.notNull(rm, "controller class must have the annotation @RequestMapping.");
			String packageUrlMapping = "";
			if(rm!=null){
				packageUrlMapping = rm.value()[0];
				if(packageUrlMapping.endsWith(SLASH)){
					packageUrlMapping = packageUrlMapping.substring(0, packageUrlMapping.length()-SLASH.length());
				}
			}

			this.packageUrlMapping = packageUrlMapping;
		}

		/**********
		 * 
		 * @param op 如果为空，不会拼凑url
		 * @param action 如果为空，不会拼凑url
		 * @return
		 */
		protected String generatePath(String op, String action){
			if(StringUtils.isBlank(op) || StringUtils.isBlank(action)){
				return this.packageUrlMapping;
			}
			return this.packageUrlMapping + op + action;
		}

		public String controller(String action){
			String actionUrl = generatePath(SLASH, action);
			if(pluginConfig!=null){
				actionUrl = pluginConfig.getContextPath()+StringUtils.appendStartWith(actionUrl, SLASH);
			}
			return actionUrl;
		}

		public String view(String page){
			String viewPath = generatePath(DEFAULT_OP, page);
			if(pluginConfig!=null){
				viewPath = pluginConfig.getTemplatePath(viewPath);
			}
			return viewPath;
		}

		public String packageDirView(String page){
			String viewPath = generatePath(SLASH, page);
			if(pluginConfig!=null){
				viewPath = pluginConfig.getTemplatePath(viewPath);
			}
			return viewPath;
		}
		
		//page
		public String getIndexView(){
			return this.packageUrlMapping;
		}

		public String getListView(){
			return view(LIST);
		}
		
		public String getShowView(){
			return view(SHOW);
		}
		
		public String getNewView(){
			return view(NEW);
		}
		
		public String getEditView(){
			return view(EDIT);
		}
		
		@SuppressWarnings("rawtypes")
		protected Object getId(Object id){
			if(id instanceof IdEntity){
				return ((IdEntity)id).getId();
			}else if(!LangUtils.isBaseType(id.getClass())){
				MappedEntryManager manager = SpringApplication.getInstance().getBeanByDefaultName(MappedEntryManager.class);
				if(manager==null)
					return id;
				JFishMappedEntry entry = manager.getEntry(id);
				if(entry==null)
					return id;
				return entry.getId(id);
			}
			return id;
		}
		//action
		
		public String getListAction(){
			String url = controller(null);
			return appendPostfix(url);
		}
		
		public String getCreateAction(){
			return getListAction();
		}
		
		public String getUpdateAction(Object id){
			return showAction(id);
		}
		
		public String showAction(Object id){
			Assert.notNull(id);
			String url = controller(getId(id).toString());
			return appendPostfix(url);
		}
		
		public String editAction(Object id){
			Assert.notNull(id);
			String url = controller(getId(id).toString()) + SLASH + EDIT;
			return appendPostfix(url);
		}
		
		public String getNewAction(){
			String url = controller(NEW);
			return appendPostfix(url);
		}
		
		public String appendPostfix(String url){
			return BaseSiteConfig.getInstance().appendAppUrlPostfix(url);
		}
		
		public String path(Object id, String type){
			String path = "";
			if(NEW.equals(type)){
				path = controller(NEW);
			}else if(EDIT.equals(type)){
				path = controller(getId(id).toString()) + SLASH + EDIT;
			}else if(CREATE.equals(type) || LIST.equals(type)){
				path = controller(null);
			}else if(UPDATE.equals(type) || SHOW.equals(type)){
				path = controller(getId(id).toString());
			}
			return path;
		}
		
		public String action(Object id, String type){
			return appendPostfix(path(id, type));
		}
		
	}
	
	public static final String REDIRECT = "redirect:";
	public static final String MESSAGE = "message";

	
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	private UrlHelper urlMeta;
	protected Class<E> entityClass;
	
	@Autowired
	private JFishPluginManager pluginManager;
	
	protected BaseController(){
		this.entityClass = ReflectUtils.getSuperClassGenricType(this.getClass());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(pluginManager);
		JFishPluginMeta meta = pluginManager.getJFishPluginMetaOf(this.getClass());
		this.urlMeta = new UrlHelper(this.getClass(), meta!=null?meta.getPluginConfig():null);
	}

	public Class<E> getEntityClass() {
		if(Object.class.equals(entityClass)){
			return null;
		}
		return entityClass;
	}

	/*****
	 * url辅助类
	 * @return
	 */
	public UrlHelper getUrlMeta() {
		return urlMeta;
	}

	/**********
	 * 根据名称返回实体的页面
	 * Deprecated see {@link #innerView}
	 * @param name
	 * @param models
	 * @return
	 */
	@Deprecated
	protected ModelAndView view(String name, Object... models){
		return mv(urlMeta.view(name), models);
	}
	/********
	 * 根据名称返回实体的页面
	 * 把类上面的url映射和name简单地用“-”连接起来
	 * @param name
	 * @param models
	 * @return
	 */
	protected ModelAndView innerView(String name, Object... models){
		return mv(urlMeta.view(name), models);
	}
	
	/********
	 * 根据名称返回实体的页面
	 * 把类上面的url映射作为目录，name作为页面
	 * 
	 * @param name
	 * @param models
	 * @return
	 */
	protected ModelAndView packageDirView(String name, Object... models){
		return mv(urlMeta.packageDirView(name), models);
	}
	
	protected String innerController(String action){
		if(action.indexOf(UrlHelper.SLASH)!=-1){
			return redirect(action);
		}else{
			return redirect(urlMeta.controller(action));
		}
	}


	
	/******
	 * 返回实体列表页面
	 * 废弃,用{@link #indexView}代替 
	 * @param models
	 * @return
	 */
	@Deprecated
	protected ModelAndView listView(Object... models){
		return mv(urlMeta.getListView(), models);
	}

	
	/******
	 * 返回实体首页页面
	 * @param models
	 * @return
	 */
	protected ModelAndView indexView(Object... models){
		return mv(urlMeta.getIndexView(), models);
	}

	
	/******
	 * 返回实体显示页面
	 * @param models
	 * @return
	 */
	protected ModelAndView showView(Object...models){
		return mv(urlMeta.getShowView(), models);
	}
	
	/******
	 * 返回实体新建页面
	 * @param models
	 * @return
	 */
	protected ModelAndView newView(Object...models){
		return mv(urlMeta.getNewView(), models);
	}

	
	/******
	 * 返回实体编辑页面
	 * @param models
	 * @return
	 */
	protected ModelAndView editView(Object...models){
		return mv(urlMeta.getEditView(), models);
	}
	
	/******
	 * 跳转到实体首页列表
	 * @return
	 */
	@Deprecated
	protected ModelAndView listAction(){
		return mv(redirect(urlMeta.getListAction()));
	}
	
	/******
	 * 重定向到实体首页列表
	 * @return
	 */
	protected ModelAndView indexAction(){
		return mv(redirect(urlMeta.getListAction()));
	}
	
	/*****
	 * 重定向到实体显示页面
	 * @param id
	 * @return
	 */
	protected ModelAndView showAction(Object id){
		return mv(redirect(urlMeta.showAction(id)));
	}
	
	/*********
	 * 重定向到实体编辑页面
	 * @param id
	 * @return
	 */
	protected ModelAndView editAction(Object id){
		return mv(redirect(urlMeta.editAction(id)));
	}
	
	/********
	 * 重定向到实体新建页面
	 * @return
	 */
	protected ModelAndView newAction(){
		return mv(redirect(urlMeta.getNewAction()));
	}
	

	/*
	protected String generatePath(String op, String action){
		Assert.hasText(op);
		Assert.hasText(action);
		String name = this.getClass().getSimpleName();
		String pack = this.getClass().getPackage().getName();
		int packIndex = pack.indexOf(CONTROLLER_PACKAGE);
		if(packIndex==-1){
			LangUtils.throwBaseException("controller must located 'controller' package : " + pack);
		}else{
			pack = pack.substring(packIndex+CONTROLLER_PACKAGE.length());
			if(pack.length()>0){
				pack = pack.substring(1) + ".";
			}
			if(pack.indexOf('.')!=-1){
				pack = pack.replace('.', '/');
			}
		}
		if(name.endsWith(CONTROLLER_POSTFIX)){
			name = StringUtils.substringBefore(name, CONTROLLER_POSTFIX);
		}
		name = StringUtils.convert2UnderLineName(name, "-");
		return pack + name + op + action;
	}
	*/
	protected String redirect(String path){
		return REDIRECT + path;
	}

	/****
	 * 历史原因存在的方法，去掉
	 * @param value
	 * @return
	 */
	@Deprecated
	protected SingleReturnWrapper json(Object value){
		return SingleReturnWrapper.wrap(value);
	}

	public void addCreateMessage(RedirectAttributes redirectAttributes){
		addFlashMessage(redirectAttributes, "保存成功！");
	}

	public void addUpdateMessage(RedirectAttributes redirectAttributes){
		addFlashMessage(redirectAttributes, "更新成功！");
	}

	public void addDeleteMessage(RedirectAttributes redirectAttributes){
		addFlashMessage(redirectAttributes, "删除成功！");
	}
	
}
