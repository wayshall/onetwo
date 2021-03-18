package org.onetwo.dbm.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author weishao zeng
 * <br/>
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DUIEntity {
	
	String name() default "";
	String label();
	
//	UIStype style() default UIStype.GRID;
	
	boolean deletable() default true;
	
	/****
	 * 详情页模式
	 * view：查看
	 * edit：编辑
	 * @author weishao zeng
	 * @return
	 */
	DetailPages detailPage() default DetailPages.EDIT;
//	boolean listPage() default true;
//	boolean editPage() default true;
	
	/****
	 * 级联编辑
	 * @author weishao zeng
	 * @return
	 */
	DUICascadeEditable[] cascadeEditableEntities() default {};
	
	DUIChildEntity[] childrenEntities() default {};
	
	public enum DetailPages {
		VIEW("查看"),
		EDIT("编辑");
		
		final private String label;
		
		private DetailPages(String label) {
			this.label = label;
		}
		
		public String getDetailName() {
			return name();
		}

		public String getLabel() {
			return label;
		}
	}
}
