package projects.manager.utils;

import java.util.stream.Stream;

final public class Enums {
	
	public static enum UserTypes {
		UNKNOW(Integer.MAX_VALUE, "未知用户级别"),
		MANAGER(0, "管理员"),
		FIRST(1, "一级用户"),
		SECOND(2, "二级用户");

		final private int value;
		final private String label;
		private UserTypes(int value, String label) {
			this.value = value;
			this.label = label;
		}
		public int getValue() {
			return value;
		}
		public String getLabel() {
			return label;
		}

		public static UserTypes of(Integer value){
			if(value==null)
				return UNKNOW;
			return Stream.of(values()).filter(s->s.value==value)
										.findAny()
										.orElse(UNKNOW);
		}
		
	}
	private Enums(){}

}
