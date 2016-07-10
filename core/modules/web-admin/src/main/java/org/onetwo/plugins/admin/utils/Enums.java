package org.onetwo.plugins.admin.utils;

import java.util.stream.Stream;

final public class Enums {

	public static interface DataKeys {
		/***
		 * 需求类型
		 */
		String DEMAND_TYPE = "DEMAND_TYPE";
		String FITMENT_TYPE = "FITMENT_TYPE";
		//租金支付方式
		String RENTAL_PAY = "RENTAL_PAY";
		//合租类型
		String SHARE_TYPE = "SHARE_TYPE";
		//整租or合租
		String RENTAL_TYPE = "RENTAL_TYPE";
		//租金级别
		String RENTAL_PRICE = "RENTAL_PRICE";
		//销售状态key
		String SELL_STATUS = "SELL_STATUS";
		//房源配套设施
		String HOUSE_FACILITY = "HOUSE_FACILITY";
	}

	public static enum ValidateCodeType {
		REGISTER("注册验证码");
		
		final private String label;

		private ValidateCodeType(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
		
	}

	public static enum DemandType {
		RENTAL_DEMAND("求租"),
		BUY_DEMAND("求购");
		
		final private String label;

		private DemandType(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public static DemandType of(String name){
			return valueOf(name.toUpperCase());
		}
	}

	public static enum HouseType {
		NEW_HOUSE("新房"),
		OLD_HOUSE("二手房"),
		RENTAL_HOUSE("出租房");
		
		final private String label;

		private HouseType(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public static HouseType of(String name){
			return valueOf(name.toUpperCase());
		}
	}

	public static enum BizType {
		NEW_HOUSE("新房"),
		OLD_HOUSE("二手房"),
		RENTAL_HOUSE("出租房"),
		RENTAL_DEMAND("求租"),
		BUY_DEMAND("求购");
		
		final private String label;

		private BizType(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public static BizType of(String name){
			return valueOf(name.toUpperCase());
		}
	}

	public static enum PublishStatus {
		DRAFT("草稿"),
		PUBLISHED("已发布"),
		CANCEL("已取消"),
		DELETE("删除");
		
		final private String label;

		private PublishStatus(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public static PublishStatus of(String name){
			return valueOf(name.toUpperCase());
		}
	}

	public static enum RentalType {
		STANDALONE("整租"),
		SHARE("合租"),
		NOLIMIT("不限");
		
		final private String label;

		private RentalType(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public static RentalType of(String name){
			return valueOf(name.toUpperCase());
		}
	}
	
	public static enum CommonStatus {
		NORMAL("正常"),
		DELETE("已删除");
		
		final private String label;

		private CommonStatus(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
		
	}
	
	public static enum UserType {
		COMMON("普通用户"),
		BROKER("经纪人");
		
		final private String label;

		private UserType(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
		
	}
	
	public static enum UserStatus {
		NORMAL("正常"),
		UNCHECK("未验证"),
		FREEZE("冻结"),
		DELETE("注销");
		
		final private String label;

		private UserStatus(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
		
		public static UserStatus of(String status){
			return Stream.of(values()).filter(s->s.name().equals(status))
										.findAny()
										.orElseThrow(()->new IllegalArgumentException("status: " + status));
		}
		
	}
	
	public static enum BrokerUserStatus {
		APPLYING("申请中"),
		APPROVED("已批准通过"),
		REJECTED("已拒绝");
		
		final private String label;

		private BrokerUserStatus(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
		
		public static BrokerUserStatus of(String status){
			return Stream.of(values()).filter(s->s.name().equals(status))
										.findAny()
										.orElseThrow(()->new IllegalArgumentException("status: " + status));
		}
		
	}
	

	public static enum NewsStatus {
		RECORD("录入"),
		APPROVED("已审核通过"),
		REJECT("审核未通过"),
		DELETE("删除");
		
		final private String label;

		private NewsStatus(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
		
		public static NewsStatus of(String status){
			return Stream.of(values()).filter(s->s.name().equals(status))
										.findAny()
										.orElseThrow(()->new IllegalArgumentException("status: " + status));
		}
		
	}
	
	
	private Enums(){}

}
