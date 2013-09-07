package org.onetwo.common.utils;

public interface CopyConf {
	/****
	 * 是否忽略null值，默认false
	 * @return
	 */
	public boolean isIgnoreNull();
	public boolean isIgnoreBlank();
	public boolean isIgnoreOther(String property, Object value);
	
	/*******
	 * 是否自动复制，默认false，如果返回true，则所有复制策略失效，通过CopyConf#copy方法来执行复制，
	 * @return
	 */
	public boolean isIgnoreAutoCopy();
	public boolean isCheckSetMethod();
	public boolean isThrowIfError();
	public String[] getIgnoreFields();
	public void copy(Object source, Object target, String property);
}
