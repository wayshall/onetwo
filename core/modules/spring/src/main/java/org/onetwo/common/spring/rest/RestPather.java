package org.onetwo.common.spring.rest;

import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.expr.Expression;
import org.onetwo.common.expr.ExpressionFacotry;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMethod;

public class RestPather {

	protected final Logger logger = JFishLoggerFactory.getLogger(RestPather.class);
	
	private Map<Class<?>, EntityPathInfo> entityPaths = LangUtils.newHashMap();
	
	public RestPather(){
	}
	
	public EntityPathInfo getEntityPathInfo(Class<?> keyClass){
		EntityPathInfo pathInfo = entityPaths.get(keyClass);
		if(pathInfo==null)
			throw new BaseException("no pathinfo :" + keyClass);
		return pathInfo;
	}
	public EntityPathInfo addEntityPathInfo(Class<?> keyClass, String entityPath, String idName){
//		if(entityPaths.containsKey(entityClass)){
////			throw new BaseException("the entity path has exist: " +entityPaths.get(entityClass));
////			System.out.println("the new entity path will override: " +entityPaths.get(entityClass));
//		}
		EntityPathInfo pathInfo = new EntityPathInfo(entityPath, idName);
		this.entityPaths.put(keyClass, pathInfo);
		return pathInfo;
	}

	public static class PathInfo {
		private final String path;
		private final String method;
		private Expression expr = ExpressionFacotry.BRACE;
		
		public PathInfo(String path, String method) {
			super();
			this.path = path;
			this.method = method.toLowerCase();
		}
		public String getPath() {
			return path;
		}
		public String getPath(Object entity) {
			return expr.parseByProvider(path, entity);
		}
		public String getMethod() {
			return method;
		}
		
		public String toString(){
			return method + ": " + path;
		}
	}
	
	public static class EntityPathInfo{
		public static final String SLASH = "/";
		public static final String DASH = "-";
//		private final Class<?> entityClass;
		
		private String entityPath;
		private String idName;
		private final PathInfo listPath;
		private final PathInfo newPath;
		private final PathInfo createPath;
		private final PathInfo batchDeletePath;
		
		private final PathInfo editPath;
		private final PathInfo showPath;
		private final PathInfo updatePath;
		private final PathInfo deletePath;
		
		public EntityPathInfo(String entityPath, String idName) {
//			this.entityClass = entityClass;
			if(entityPath.endsWith(SLASH)){
				this.entityPath = entityPath.substring(0, entityPath.length()-SLASH.length());
			}else{
				this.entityPath = entityPath;
			}
			if(StringUtils.isBlank(idName)){
				this.idName = "id";
			}
			this.idName = idName;
			this.listPath = new PathInfo(entityPath, RequestMethod.GET.toString());
			this.newPath = new PathInfo(entityPath+"/new", RequestMethod.GET.toString());
			this.createPath = new PathInfo(entityPath, RequestMethod.POST.toString());
			this.batchDeletePath = new PathInfo(entityPath, RequestMethod.DELETE.toString());
			
			String idPath = "/{"+this.idName+"}";
			this.showPath = new PathInfo(entityPath+idPath, RequestMethod.GET.toString());
			this.updatePath = new PathInfo(entityPath+idPath, RequestMethod.PUT.toString());
			this.editPath = new PathInfo(entityPath+idPath+"/edit", RequestMethod.GET.toString());
			this.deletePath = new PathInfo(entityPath+idPath, RequestMethod.DELETE.toString());
		}
		

		public String dashView(String action){
			return generatePath(DASH, action);
		}
		public String dirView(String action){
			return generatePath(SLASH, action);
		}

		public String generatePath(String op, String action){
			if(StringUtils.isBlank(op) || StringUtils.isBlank(action)){
				return this.entityPath;
			}
			return this.entityPath + op + action;
		}

		public PathInfo getListPathInfo() {
			return listPath;
		}

		public PathInfo getNewPathInfo() {
			return newPath;
		}

		public PathInfo getCreatePathInfo() {
			return createPath;
		}

		public PathInfo getBatchDeletePathInfo() {
			return batchDeletePath;
		}

		public PathInfo getEditPathInfo() {
			return editPath;
		}

		public PathInfo getShowPathInfo() {
			return showPath;
		}

		public PathInfo getUpdatePathInfo() {
			return updatePath;
		}

		public PathInfo getDeletePathInfo() {
			return deletePath;
		}
		
		public String toString(){
			return entityPath + ", " + idName;
		}
		
	}

}
