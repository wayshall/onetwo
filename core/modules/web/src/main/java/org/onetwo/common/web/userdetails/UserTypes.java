package org.onetwo.common.web.userdetails;

import java.util.stream.Stream;

import org.onetwo.common.convert.EnumerableTextLabel;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weishao zeng
 * <br/>
 */
@AllArgsConstructor
public enum UserTypes implements EnumerableTextLabel, UserType {
	SYSTEM("系统"),
	ADMIN_USER("后台用户"),
	USER("前台用户");
	
	@Getter
	private String label;

	
	public static UserTypes of(String userType){
		return Stream.of(values()).filter(s->s.name().equalsIgnoreCase(userType))
									.findAny()
									.orElseThrow(()->new IllegalArgumentException("error userType: " + userType));
	}
	
}
