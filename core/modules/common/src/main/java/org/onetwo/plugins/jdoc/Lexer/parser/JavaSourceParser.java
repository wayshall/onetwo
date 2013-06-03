package org.onetwo.plugins.jdoc.Lexer.parser;

import java.io.File;
import java.util.List;

import org.onetwo.common.lexer.FileSourceReader;
import org.onetwo.common.lexer.JParser;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.jdoc.Lexer.JLexer;
import org.onetwo.plugins.jdoc.Lexer.JSourceParser;
import org.onetwo.plugins.jdoc.Lexer.JToken;
import org.onetwo.plugins.jdoc.Lexer.defined.AnnotationDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.ClassDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.JDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.JavaClassDefineImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.SimpleDefinedImpl;

public class JavaSourceParser extends JSourceParser {

	private final File source;
	public JavaSourceParser(File file) {
		super(new JLexer(new FileSourceReader(file)));
		this.source = file;
		addDefineParser(new SemiParser(getLexer(), JToken.PACKAGE) {

			@Override
			protected JDefinedImpl newJDefined(JToken definedType, String str) {
				getLexer().nextToken();
//				throwIfNoNextToken();package-info only a pckage code
				return new SimpleDefinedImpl(definedType, str);
			}
		}).addDefineParser(new SemiParser(getLexer(), JToken.IMPORT) {

			@Override
			protected JDefinedImpl newJDefined(JToken definedType, String str) {
				throwIfNoNextToken();
				return new SimpleDefinedImpl(definedType, str);
			}
		}).addDefineParser(new AnnotationParser(getLexer())).addDefineParser(new ClassParser(getLexer()));
	}
	
	public JavaClassDefineImpl parse(JDefinedImpl parent) {
		JToken token = null;

		JavaClassDefineImpl jcd = new JavaClassDefineImpl(source);

		lexer.nextToken();
		JParser<JDefinedImpl> parser = null;
		JDefinedImpl define = null;
		String document = "";
		final List<AnnotationDefinedImpl> annos = LangUtils.newArrayList();
		while (true) {
			token = lexer.getToken();
			if (tokenIs(JToken.JAVADOC)) {
				document = stringValue();
				throwIfNoNextToken();
			} else {
				parser = matchDefineParser(token);
				if (parser != null) {
					
					/*if(tokenIs(JToken.PUBLIC)){
						System.out.println("test");
					}*/
					define = parser.parse(jcd);
					if(ClassDefinedImpl.class.isInstance(define)){
						ClassDefinedImpl clsDefine = (ClassDefinedImpl) define;
						clsDefine.setDocument(document);
						clsDefine.addAnnotations(annos);
						document="";
						annos.clear();
					}
					if (define != null) {
						if(AnnotationDefinedImpl.class.isInstance(define)){
							annos.add((AnnotationDefinedImpl)define);
						}else{
							jcd.addDefine(define);
						}
					}
				} else if (token == JToken.EOF) {
					break;
				} else {
					// Ignore
					lexer.nextToken();
				}
			}
		}

		return jcd;
	}

}
