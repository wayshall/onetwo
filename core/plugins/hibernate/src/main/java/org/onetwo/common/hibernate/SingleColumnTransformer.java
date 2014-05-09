package org.onetwo.common.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.convert.Types;

public class SingleColumnTransformer extends AliasedTupleSubsetResultTransformer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3068449276325782557L;
	private final Class<?> resultClass;

	public SingleColumnTransformer(Class<?> resultClass) {
		if ( resultClass == null ) {
			throw new IllegalArgumentException( "resultClass cannot be null" );
		}
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
			result = Types.convertValue(LangUtils.isEmpty(tuple)?null:tuple[0], resultClass);
		}
		catch ( Exception e ) {
			throw new HibernateException( "Could not transform result : " + resultClass.getName() );
		}

		return result;
	}

	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		SingleColumnTransformer that = ( SingleColumnTransformer ) o;

		if ( ! resultClass.equals( that.resultClass ) ) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result = resultClass.hashCode();
		return result;
	}
}
