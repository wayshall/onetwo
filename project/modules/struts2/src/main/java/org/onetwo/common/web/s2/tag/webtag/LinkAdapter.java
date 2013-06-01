package org.onetwo.common.web.s2.tag.webtag;

public interface LinkAdapter {

	public void adapter(Object obj);

	public Object getLinkValue();
	
	public boolean needParseLink();

	public String getText();

	public String getTitle();

	public String getTarget();

	public String getImage();

}
