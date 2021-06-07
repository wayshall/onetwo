package org.onetwo.common.convert;

public interface TypeConvert<T> {

	/****
	 * 
	 * @author weishao zeng
	 * @param source
	 * @param componentType
	 * @return 如果source为null，则返回配置的默认值
	 */
	public T convertNotNull(Object source, Class<?> componentType);
	
	/****
	 * 当目标类型为基本类型时，如果source为null，则返回配置的基本类型的默认值
	 * 否则，返回null
	 * @author weishao zeng
	 * @param source
	 * @param componentType
	 * @return
	 */
	public T convert(Object source, Class<?> componentType);
	
}
