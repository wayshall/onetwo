package org.onetwo.plugins.jdoc.Lexer.parser;

import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.jdoc.Lexer.JLexer;
import org.onetwo.plugins.jdoc.Lexer.JToken;
import org.onetwo.plugins.jdoc.Lexer.defined.AnnotationDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.ClassDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.FieldDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.JDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.JavaClassDefineImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.MethodDefinedImpl;

public class ClassParser extends AbstractDefinedParser {


//	private List<DefinedParser> defineParsers = new ArrayList<DefinedParser>();
	
	private AnnotationParser annotationParser;
	private MethodOrFieldParser methodOrFieldParser;
	
	public ClassParser(JLexer lexer) {
		super(lexer, JToken.PUBLIC, 
				JToken.ABSTRACT,
				JToken.FINAL,
				JToken.CLASS);
//		addDefineParser(new AnnotationParser(lexer))
//		.addDefineParser(new MethodOrFieldParser(lexer));
		annotationParser = new AnnotationParser(lexer);
		methodOrFieldParser = new MethodOrFieldParser(lexer);
	}
	


//	final public ClassParser addDefineParser(DefinedParser parser){
//		defineParsers.add(parser);
//		return this;
//	}
//	
//	final public JParser matchDefineParser(JToken token){
//		for(DefinedParser p : defineParsers){
//			if(p.isDefinedMatch(token))
//				return p;
//		}
//		return null;
//	}
	

	public ClassDefinedImpl parse(JDefinedImpl parent) {
		List<JToken> kws = nextAllKeyWordTokens();
		String name = lexer.getStringValue();
		ClassDefinedImpl jdf = new ClassDefinedImpl((JavaClassDefineImpl)parent, name, kws.toArray(new JToken[kws.size()]));
		
		int braceCount = 0;
		String document = "";
		List<AnnotationDefinedImpl> annos = LangUtils.newArrayList();
		this.throwIfNoNextToken();
		while (true) {
			if (tokenIs(JToken.LBRACE)) {
				braceCount++;
			} else if (tokenIs(JToken.RBRACE)) {
				braceCount--;
			}else if(lexer.getToken() == JToken.JAVADOC){
				document = stringValue();
			}else if(lexer.getToken()==JToken.AT){
				annos.add(this.annotationParser.parse(jdf));
				continue;
			}else{
				if(braceCount==0){
					//不检查extends implements 
					this.throwIfNoNextToken();
					continue;
				}
				
				JDefinedImpl mjdf = this.methodOrFieldParser.parse(jdf);
				if(FieldDefinedImpl.class.isInstance(mjdf)){
					FieldDefinedImpl field = (FieldDefinedImpl) mjdf;
					field.setDocument(document);
					field.addAnnotations(annos);
					if(!field.isSerialVersionUID()){
						jdf.addField(field);
					}
					document = "";
					annos.clear();
				}else if(MethodDefinedImpl.class.isInstance(mjdf)){
					MethodDefinedImpl m = (MethodDefinedImpl) mjdf;
					m.addAnnotations(annos);
					m.setDocument(document);
					jdf.addMethod(m);
					document = "";
					annos.clear();
				}
			}
			if (braceCount == 0) {
				break;
			}
			this.lexer.nextToken();
			if(tokenIs(JToken.EOF))
				break;
		}
		return jdf;
	}

}
