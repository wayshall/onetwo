package org.onetwo.common.utils;


public class CopyConfig implements CopyConf {
	
	public static CopyConfig create() {
		return new CopyConfig();
	};

	private boolean ignoreNull = false;
	private boolean ignoreBlank = false;
	private boolean ignoreOther = false;
	private boolean ignoreAutoCopy = false;
	private boolean throwIfError = false;
	private boolean checkSetMethod = false;
	private String[] ignoreFields;
	
	@Override
	public boolean isIgnoreNull() {
		return ignoreNull;
	}

	@Override
	public boolean isIgnoreBlank() {
		return ignoreBlank;
	}

	@Override
	public boolean isIgnoreOther(String property, Object value) {
		return ignoreOther;
	}

	@Override
	public boolean isIgnoreAutoCopy() {
		return ignoreAutoCopy;
	}

	@Override
	public void copy(Object source, Object target, String property) {
		throw new UnsupportedOperationException();
	}

	public CopyConfig ignoreNull() {
		this.ignoreNull = true;
		return this;
	}

	public CopyConfig ignoreBlank() {
		this.ignoreBlank = true;
		return this;
	}

	public CopyConfig ignoreAutoCopy() {
		this.ignoreAutoCopy = true;
		return this;
	}

	public String[] getIgnoreFields() {
		return ignoreFields;
	}

	public CopyConfig ignoreFields(String... ignoreFields) {
		this.ignoreFields = ignoreFields;
		return this;
	}

	public boolean isThrowIfError() {
		return throwIfError;
	}

	public CopyConfig throwIfError() {
		this.throwIfError = true;
		return this;
	}

	public boolean isCheckSetMethod() {
		return checkSetMethod;
	}

	public CopyConfig checkSetMethod() {
		this.checkSetMethod = true;
		return this;
	}
	
	
}
