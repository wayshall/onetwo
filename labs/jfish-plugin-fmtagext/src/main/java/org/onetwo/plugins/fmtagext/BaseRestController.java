package org.onetwo.plugins.fmtagext;

import org.onetwo.common.spring.rest.RestPather;
import org.onetwo.common.spring.rest.RestPather.EntityPathInfo;
import org.onetwo.common.spring.rest.RestPather.PathInfo;
import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.utils.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

abstract public class BaseRestController<T> extends AbstractBaseController {
	
	private Class<T> entityClass;
	@Autowired
	private RestPather restPather;
	
	protected BaseRestController(){
		this.entityClass = ReflectUtils.getSuperClassGenricType(this.getClass());
	}

	public String getBaseEntityPath(){
		return null;
	}
	
	//build page
	public void initBuild(){
		
	}

	
	protected ModelAndView redirectTo(PathInfo path){
		return mv(redirect(path.getPath()));
	}
	
	public Class<T> getEntityClass() {
		return entityClass;
	}

	public EntityPathInfo getEntityPathInfo() {
		return this.restPather.getEntityPathInfo(this.getClass());
	}

	public RestPather getRestPather() {
		return restPather;
	}

}
