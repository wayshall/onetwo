package org.onetwo.common.hibernate;

import java.util.Arrays;

import org.hibernate.HibernateException;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.onetwo.common.spring.SpringUtils;
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

	public RowToBeanTransformer(Class<?> resultClass) {
		if ( resultClass == null ) {
			throw new IllegalArgumentException( "resultClass cannot be null" );
		}
		isInitialized = false;
		this.resultClass = resultClass;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
		return false;
	}	

	public Object transformTuple(Object[] tuple, String[] aliases) {
		Object result;

		try {
			if ( ! isInitialized ) {
				initialize( aliases );
			}
			else {
				check( aliases );
			}
			
			result = resultClass.newInstance();
			BeanWrapper bw = SpringUtils.newBeanWrapper(result);

			Object val;
			for ( int i = 0; i < aliases.length; i++ ) {
				val = Types.convertValue(tuple[i], bw.getPropertyType(propNames[i]));
				bw.setPropertyValue(propNames[i], val);
			}
		}
		catch ( InstantiationException e ) {
			throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
		}
		catch ( IllegalAccessException e ) {
			throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
		}

		return result;
	}

	private void initialize(String[] aliases) {
		this.aliases = new String[ aliases.length ];
		this.propNames = new String[ aliases.length ];
		for ( int i = 0; i < aliases.length; i++ ) {
			String alias = aliases[ i ];
			if ( alias != null ) {
				this.aliases[i] = alias;
				this.propNames[i] = StringUtils.toPropertyName(alias); 
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
}
