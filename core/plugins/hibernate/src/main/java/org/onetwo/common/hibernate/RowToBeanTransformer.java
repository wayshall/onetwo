package org.onetwo.common.hibernate;

import java.sql.Clob;
import java.util.Arrays;
import java.util.List;

import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.convert.Types;
import org.springframework.beans.BeanWrapper;

public class RowToBeanTransformer extends AliasedTupleSubsetResultTransformer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3068449276325782557L;
	private final Class<?> resultClass;
	private boolean isInitialized;
	private String[] aliases;
	private String[] propNames;
	private boolean checkAlias;

	private boolean tupleResult;


	public RowToBeanTransformer(Class<?> resultClass) {
		this(resultClass, true);
	}
	public RowToBeanTransformer(Class<?> resultClass, boolean checkAlias) {
		if ( resultClass == null ) {
			throw new IllegalArgumentException( "resultClass cannot be null" );
		}
		isInitialized = false;
		this.resultClass = resultClass;
		this.checkAlias = checkAlias;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
		return false;
	}	

	public Object transformTuple(Object[] tuple, String[] aliases) {
		if(tupleResult)
			return tuple;
		
		Object result;

		String propName = "";
		try {
			if ( ! isInitialized ) {
				initialize( aliases );
			}
			else {
				if(checkAlias)//如果是sqlserver游标的方式，第一次之后不能获取列名，没必要重复检查
					check( aliases );
			}
			
			try {
				result = resultClass.newInstance();
			} catch (Exception e) {
				//direct return tuple result if error.
				tupleResult = true;
				return tuple;
			}
			
			BeanWrapper bw = SpringUtils.newBeanWrapper(result);

			Object val;
			for ( int i = 0; i < aliases.length; i++ ) {
				propName = propNames[i];
				/*if(!bw.isWritableProperty(propName))
					continue;*/
				if(propName==null)
					continue;
				val = tuple[i];
				
				Class<?> propertyType = bw.getPropertyType(propName);
//					if(propertyType!=null && !propertyType.isInstance(val))
				if(propertyType!=null && !Clob.class.isInstance(val))
					val = Types.convertValue(val, propertyType);
				/*if(val==null){
					Class<?> propertyType = bw.getPropertyType(propNames[i]);
					if(propertyType!=null && propertyType.isPrimitive())
						continue;
				}*/
				bw.setPropertyValue(propName, val);
			}
		}
		catch ( Exception e ) {
			throw new BaseException( "set bean["+resultClass.getName()+"] property["+propName+"] value error: " + e.getMessage() );
		}

		return result;
	}

	private void initialize(String[] aliases) {
		Assert.notEmpty(aliases, "aliases is emtpy!");
		this.aliases = new String[ aliases.length ];
		this.propNames = new String[ aliases.length ];

		List<String> resultPropNames = ReflectUtils.desribPropertiesName(resultClass);
		
		for ( int i = 0; i < aliases.length; i++ ) {
			String alias = aliases[ i ];
			if ( alias != null ) {
				this.aliases[i] = alias;
				String propName = StringUtils.toPropertyName(alias); 
				if(resultPropNames.contains(propName)){
					this.propNames[i] = propName;
				}
			}
		}
		isInitialized = true;
	}

	private void check(String[] aliases) {
		if ( ! Arrays.equals( aliases, this.aliases ) ) {
			throw new IllegalStateException(
					"aliases are different from what is cached; aliases=" + Arrays.asList( aliases ) +
							" cached=" + Arrays.asList( this.aliases ) );
		}
	}

	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		RowToBeanTransformer that = ( RowToBeanTransformer ) o;

		if ( ! resultClass.equals( that.resultClass ) ) {
			return false;
		}
		if ( ! Arrays.equals( aliases, that.aliases ) ) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result = resultClass.hashCode();
		result = 31 * result + ( aliases != null ? Arrays.hashCode( aliases ) : 0 );
		return result;
	}

	public void setCheckAlias(boolean checkAlias) {
		this.checkAlias = checkAlias;
	}
	
}
