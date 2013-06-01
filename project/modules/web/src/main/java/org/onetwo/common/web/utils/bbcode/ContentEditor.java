package org.onetwo.common.web.utils.bbcode;

public interface ContentEditor {
	
	/**
	 * ubb编辑器
	 */
	public static final int EDITOR_UBB = 0;
	/***
	 * fck编辑器
	 */
	public static final int EDITOR_HTML = 1;
	
	/***
	 * 返回编辑器的类型
	 * @return
	 */
	public Integer getEditorType();

}
