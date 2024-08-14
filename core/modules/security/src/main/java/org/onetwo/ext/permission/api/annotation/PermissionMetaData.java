package org.onetwo.ext.permission.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/****
 * 用于设置vue路由的meta属性的数据
 * {
 * 		meta: {
		  roles: ['admin', 'editor'] // 设置该路由进入的权限，支持多个权限叠加
		  title: 'title' // 设置该路由在侧边栏和面包屑中展示的名字
		  icon: 'svg-name' // 设置该路由的图标，支持 svg-class，也支持 el-icon-x element-ui 的 icon
		  noCache: true // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
		  breadcrumb: false //  如果设置为false，则不会在breadcrumb面包屑中显示(默认 true)
		  affix: true // 若果设置为true，它则会固定在tags-view中(默认 false)
		
		  // 当路由设置了该属性，则会高亮相对应的侧边栏。
		  // 这在某些场景非常有用，比如：一个文章的列表页路由为：/article/list
		  // 点击文章进入文章详情页，这时候路由为/article/1，但你想在侧边栏高亮文章列表的路由，就可以进行如下设置
		  activeMenu: '/article/list'
 * 		}
 * }
 * meta.put(field.getName(), fieldValue)
 * 特殊名称的属性会被特殊处理，如router属性，只能定义为RouteData类型，主要用于定义组件路径和props
 * @author way
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionMetaData {
}
