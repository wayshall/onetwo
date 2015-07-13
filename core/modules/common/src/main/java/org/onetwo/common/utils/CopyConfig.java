package org.onetwo.common.utils;


public class CopyConfig {
	
	public static CopyConfig create() {
		return new CopyConfig();
	};
	
	public static CopyConfig createIgnoreBlank(String...ignoreFields) {
		return CopyConfig.create().ignoreIfNoSetMethod().ignoreNull().ignoreBlank().ignoreFields(ignoreFields);
	};

	private boolean ignoreNull = false;
	private boolean ignoreBlank = false;
	private boolean ignoreOther = false;
//	private boolean ignoreAutoCopy = false;
	private boolean throwIfError = false;
	private boolean ignoreIfNoSetMethod = false;
	private String[] ignoreFields;
	private String[] includeFields;
	
//	@Override
	public boolean isIgnoreNull() {
		return ignoreNull;
	}

//	@Override
	public boolean isIgnoreBlank() {
		return ignoreBlank;
	}

//	@Override
	public boolean isIgnoreOther(String property, Object value) {
		return ignoreOther;
	}

	public CopyConfig ignoreNull() {
		this.ignoreNull = true;
		return this;
	}

	public CopyConfig ignoreBlank() {
		this.ignoreBlank = true;
		return this;
	}

	/*public CopyConfig ignoreAutoCopy() {
		this.ignoreAutoCopy = true;
		return this;
	}*/

	public String[] getIgnoreFields() {
		return ignoreFields;
	}

	public CopyConfig ignoreFields(String... ignoreFields) {
		this.ignoreFields = ignoreFields;
		return this;
	}

	public CopyConfig includeFields(String... includeFields) {
		this.includeFields = includeFields;
		return this;
	}

	public String[] getIncludeFields() {
		return includeFields;
	}

	public boolean isThrowIfError() {
		return throwIfError;
	}

	public CopyConfig throwIfError() {
		this.throwIfError = true;
		return this;
	}

	public boolean isIgnoreIfNoSetMethod() {
		return ignoreIfNoSetMethod;
	}

	public CopyConfig ignoreIfNoSetMethod() {
		this.ignoreIfNoSetMethod = true;
		return this;
	}
	
	
}
