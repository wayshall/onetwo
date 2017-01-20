package org.onetwo.dbm.richmodel;

import javassist.CtClass;

public class EnhanceContext {

	private final String className;
	private final CtClass ctClass;

	public EnhanceContext(String className, CtClass ctClass) {
		super();
		this.className = className;
		this.ctClass = ctClass;
	}

	public String getClassName() {
		return className;
	}

	public CtClass getCtClass() {
		return ctClass;
	}

}
