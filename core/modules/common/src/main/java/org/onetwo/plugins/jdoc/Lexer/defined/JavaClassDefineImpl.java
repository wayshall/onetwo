package org.onetwo.plugins.jdoc.Lexer.defined;

import java.io.File;
import java.util.List;

import org.onetwo.common.lexer.JSyntaxException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.jdoc.Lexer.JToken;

/***
 * 
 * @author way
 * 
 */
public class JavaClassDefineImpl extends JDefinedImpl {
	
	private File file;
	private JDefinedImpl packageDefine;
	private ClassDefinedImpl publicClassDefine;
	private final List<JDefinedImpl> imports = LangUtils.newArrayList();
	private final List<ClassDefinedImpl> classDefineds = LangUtils.newArrayList();

	public JavaClassDefineImpl(File file) {
		super("", JToken.PUBLIC, JToken.CLASS);
		this.file = file;
	}

	public void addDefine(JDefinedImpl define) {
		if (define == null)
			return;
		if (define.isDefineBy(JToken.PACKAGE)) {
			if (packageDefine == null) {
				packageDefine = define;
			} else {
				throw new JSyntaxException("too many package define.");
			}
		} else if (define.isDefineBy(JToken.IMPORT)) {
			addImport(define);
		}/* else if (define.isDefineBy(JToken.AT)) {
			addAnnotation((AnnotationDefinedImpl)define);
		}*/ else if (define.isDefineBy(JToken.CLASS)) {
			if (define.isDefineBy(JToken.PUBLIC)) {
				if (publicClassDefine == null) {
					publicClassDefine = (ClassDefinedImpl)define;
					this.name = publicClassDefine.getName();
				} else {
					throw new JSyntaxException("too many public class define.");
				}
			}else{
				this.classDefineds.add((ClassDefinedImpl)define);
			}
		} else {
			// ignore
		}
	}

	public JDefinedImpl getPackageDefine() {
		return packageDefine;
	}


	public ClassDefinedImpl getPublicClassDefine() {
		return publicClassDefine;
	}
	
/*
	public void addAnnotation(AnnotationDefinedImpl anno) {
		if (anno == null)
			return;
		if(LangUtils.isNotEmpty(classDefineds)){
			classDefineds.get(classDefineds.size()-1).addAnnotation(anno);
		}else{
			super.addAnnotation(anno);
		}
	}*/

	public List<ClassDefinedImpl> getClassDefineds() {
		return classDefineds;
	}

	public List<JDefinedImpl> getImports() {
		return imports;
	}

	public void addImport(JDefinedImpl jdf) {
		if (jdf == null)
			return;
		this.imports.add(jdf);
	}

	public String getFullClassNameFromImport(String shortName){
		for(JDefinedImpl jdf : this.imports){
			if(jdf.getName().endsWith("."+shortName))
				return jdf.getName();
		}
		return shortName;
	}

	public File getFile() {
		return file;
	}


}
