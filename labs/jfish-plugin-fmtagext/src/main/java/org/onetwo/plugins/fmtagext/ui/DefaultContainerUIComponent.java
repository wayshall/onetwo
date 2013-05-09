package org.onetwo.plugins.fmtagext.ui;

import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class DefaultContainerUIComponent implements ContainerUIComponent, RendableUI {

	private List<UIComponent> children;
	private final String wrapTag;
	private final boolean wrapHtml;
	private boolean directedRenderText;
	
	public DefaultContainerUIComponent(String wrapTag) {
		super();
		this.wrapTag = wrapTag;
		this.wrapHtml = StringUtils.isNotBlank(wrapTag);
		if(wrapHtml){
			this.directedRenderText = wrapTag.startsWith("&");
		}
	}

	@Override
	public List<UIComponent> getChildren() {
		return children;
	}

	public void addChild(UIComponent uiComponent){
		if(this.children==null){
			this.children = LangUtils.newArrayList();
		}
		this.children.add(uiComponent);
	}

	@Override
	public boolean isEmpty() {
		return LangUtils.isEmpty(children);
	}
	
	protected void renderStartTag(UIRender render){
		if(directedRenderText)
			render.renderString(wrapTag);
		else
			render.renderString("<"+wrapTag+">");
	}
	
	protected void renderEndTag(UIRender render){
		if(directedRenderText)
			render.renderString(wrapTag);
		else
			render.renderString("</"+wrapTag+">");
	}

	@Override
	public void onRender(UIRender render) {
		if(isEmpty())
			return ;
		for(UIComponent uic : children){
			if(wrapHtml)
				renderStartTag(render);
			render.renderUIComponent(uic);
			if(wrapHtml)
				renderEndTag(render);
		}
	}
}
