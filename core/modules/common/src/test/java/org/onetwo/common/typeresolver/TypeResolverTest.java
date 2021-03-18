package org.onetwo.common.typeresolver;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.onetwo.common.reflect.Intro;
import org.onetwo.common.reflect.ReflectUtils;

import net.jodah.typetools.TypeResolver;

/**
 * @author wayshall
 * <br/>
 */
public class TypeResolverTest {
	
	private List<Integer> intList;
	
	@Test
	public void test(){
		List<Integer> list = new ArrayList<Integer>();
		Class<?> type = TypeResolver.resolveRawArgument(List.class, list.getClass());
		System.out.println("type:"+type);
		
		list = new ArrayList<Integer>(){};
		type = TypeResolver.resolveRawArgument(List.class, list.getClass());
		System.out.println("type:"+type);
		
		Class<?> propertyType = Intro.wrap(this.getClass()).getProperty("intList").getPropertyType();
		System.out.println("propertyType: " + propertyType);
		
		Class<?> listItemType = ReflectUtils.getListGenricType(propertyType);
		System.out.println("listItemType: " + listItemType);
		
		Type gtype = TypeResolver.resolveGenericType(EnumValueMapping.class, UserGenders.MALE.getClass());
		System.out.println("gtype:"+gtype);
		type = TypeResolver.resolveRawClass(gtype, UserGenders.MALE.getClass());
		System.out.println("type:"+type);
		
		type = ReflectUtils.getGenricType(gtype, 0);
		System.out.println("type:"+type);
		assertThat(type).isEqualTo(Double.class);
		

		type = ReflectUtils.resolveClassOfGenericType(EnumValueMapping.class, UserGenders.MALE.getClass());
		System.out.println("type:"+type);
		assertThat(type).isEqualTo(Double.class);
		
	}

	public List<Integer> getIntList() {
		return intList;
	}

	public void setIntList(List<Integer> intList) {
		this.intList = intList;
	}
	
	public static enum UserGenders implements EnumValueMapping<Double> {
		FEMALE("女性", 10),
		MALE("男性", 11);
		
		final private String label;
		final private double value;
		private UserGenders(String label, double value) {
			this.label = label;
			this.value = value;
		}
		public String getLabel() {
			return label;
		}
		@Override
		public Double getEnumMappingValue() {
			return value;
		}
		
	}
	
	public interface EnumValueMapping<T> {
		
		T getEnumMappingValue();

	}

}
