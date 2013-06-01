package org.onetwo.plugins.jdoc.Lexer.parser;

import java.util.List;

import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.plugins.jdoc.Lexer.JLexer;
import org.onetwo.plugins.jdoc.Lexer.JToken;
import org.onetwo.plugins.jdoc.Lexer.defined.ClassDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.FieldDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.JDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.MethodDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.MethodDefinedImpl.MethodParam;

public class MethodOrFieldParser extends AbstractDefinedParser {

	private static final JToken[] FIELD_DEFINE = new JToken[]{
			JToken.PUBLIC,
			JToken.PRIVATE,
			JToken.PROTECTED, 
			JToken.STATIC,
			JToken.FINAL
	};
	private static JToken[] METHOD_DEFINE = (JToken[])ArrayUtils.add(FIELD_DEFINE, JToken.ABSTRACT);
	private static JToken[] ClASS_DEFINE = (JToken[])ArrayUtils.add(METHOD_DEFINE, JToken.CLASS);
	
	private final AnnotationParser annotationParser;
	public MethodOrFieldParser(JLexer lexer) {
		super(lexer);
		this.annotationParser = new AnnotationParser(lexer);
	}

	@Override
	public JDefinedImpl parse(JDefinedImpl parent) {
		List<JToken> kws = nextAllTheseTokens(ClASS_DEFINE);
		if(kws.contains(JToken.CLASS)){
			//ignroe inner class
			this.ignoreBlock();
			return null;
		}
		if(tokenIs(JToken.LBRACE)){//static block
			this.ignoreBlock(1);
			return null;
		}
		JTokenValueCollection<JToken> typeAndName = nextAllTokensUntil(JToken.SEMI, JToken.LPARENT, JToken.ASSIGN);
		
		String type = typeAndName.getValuesExceptLast();
		String name = typeAndName.getLast().value;
		/*try {
			name = typeAndName.getLast().value;
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		/*
		if(tokenIs(JToken.LPARENT)){
			//constructor
			this.ignoreBlock();
			return null;
		}*/
		/*if(!tokenIs(JToken.IDENTIFIER)){
			throw new JSyntaxException();
		}
		*/
		
//		this.throwIfNoNextToken();
		
		if(tokenIs(JToken.SEMI)){
			//field
			FieldDefinedImpl field = new FieldDefinedImpl(name, kws.toArray(new JToken[kws.size()]));
			field.setDeclareType(type);
			return field;
			
		} else if(tokenIs(JToken.ASSIGN)){
			FieldDefinedImpl field = new FieldDefinedImpl(name, kws.toArray(new JToken[kws.size()]));
			field.setDeclareType(type);
			nextAllTokensUntil(JToken.SEMI);
			return field;
			
		}
//		else if(tokenIs(JToken.LPARENT)){
		else{
			MethodDefinedImpl method = new MethodDefinedImpl((ClassDefinedImpl)parent, name, kws.toArray(new JToken[kws.size()]));
			method.setReturnType(type);
			while(true){
				this.throwIfNoNextToken();
				if(tokenIs(JToken.RPARENT)){//方法结束
					break;
				}else{
					//扫描方法参数
					MethodParam p = new MethodParam(method, null);
					if(tokenIs(JToken.AT)){
						p.addAnnotation(this.annotationParser.parse(p));
					}
					
					JTokenValueCollection<JToken> ids = nextAllTokensUntil(JToken.COMMA, JToken.RPARENT);
					String pname = ids.getLast().value;
					String ptype = ids.getValuesExceptLast();
					p.setDeclareType(ptype);
					p.setName(pname);
					method.getParameters().add(p);
					if(tokenIs(JToken.RPARENT))
						break;
				}
			}
			this.throwIfNoNextToken();
			if(tokenIs(JToken.SEMI)){//方法结束：接口或者抽象方法
				return method;
			}else if(tokenIs(JToken.THROWS)){
				this.ignoreBlock();
				return method;
			}else if(tokenIs(JToken.LBRACE)){//忽略方法体
				this.ignoreBlock(1);
				if(method.isConstructor() || !method.isPublic())
					return null;
				return method;
			}else{
				return null;
			}
		}/*else{
			throw new JSyntaxException("java syntax error : " + lexer.getToken()+","+stringValue());
		}*/

	}

}
