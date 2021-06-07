package org.onetwo.common.spring.ftl;

/***
 * @author way
 *
 */
public class NameIsTemplateConfigurer extends DynamicStringFreemarkerTemplateConfigurer {
	
	public static final NameIsTemplateConfigurer INSTANCE;
	static {
		INSTANCE = new NameIsTemplateConfigurer();
		INSTANCE.initialize();
	}

	public NameIsTemplateConfigurer() {
		super(new NameIsContentTemplateProvider());
	}
	
	public static class NameIsContentTemplateProvider implements StringTemplateProvider {

		@Override
		public String getTemplateContent(String name) {
			return name;
		}
	}

}
