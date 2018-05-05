package org.onetwo.boot.core.config;

import lombok.Builder;
import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
public enum PluginContextPathModes {
	/***
	 * 统一加插件路径前缀
	 */
	APPEND(){
		public String getPluginContextPath(PathContext ctx){
			return ctx.getPluginContextPath();
		}
	},
	/***
	 * 不加插件路径
	 */
	NO_APPEND,
	/***
	 * 检查controller的路径是否以插件路径开始，如果是则不添加插件路径，否则添加插件路径
	 
	AUTO(){
		public String getPluginContextPath(PathContext ctx){
			//如果路径不是以插件前缀开始，则自动加插件前缀
			final String contextPathWithSlash = StringUtils.appendEndWithSlash(ctx.pluginContextPath);
			if(!ctx.getControllerPath().startsWith(contextPathWithSlash)){
				return ctx.getPluginContextPath();
			}
			return null;
		}
	}*/
	;

	public String getPluginContextPath(PathContext ctx){
		return null;
	}
	
	@Data
	@Builder
	public static class PathContext {
		String pluginContextPath;
//		String controllerPath;
	}
}
