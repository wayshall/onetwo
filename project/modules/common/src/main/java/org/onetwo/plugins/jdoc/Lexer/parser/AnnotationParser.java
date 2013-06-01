package org.onetwo.plugins.jdoc.Lexer.parser;

import org.onetwo.common.lexer.JSyntaxException;
import org.onetwo.plugins.jdoc.Lexer.JLexer;
import org.onetwo.plugins.jdoc.Lexer.JToken;
import org.onetwo.plugins.jdoc.Lexer.defined.AnnotationDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.JDefinedImpl;

public class AnnotationParser extends AbstractDefinedParser {

	public AnnotationParser(JLexer lexer) {
		super(lexer, JToken.AT);
	}

	@Override
	public AnnotationDefinedImpl parse(JDefinedImpl parent) {
		this.throwIfNextTokenIsNot(JToken.IDENTIFIER, stringValue());
		AnnotationDefinedImpl anno = new AnnotationDefinedImpl(lexer.getStringValue());

		if(!nextTokenIs(JToken.LPARENT))
			return anno;
//		tokenIs(JToken.LPARENT);
		while (this.throwIfNextTokenIsNotOneOf(JToken.IDENTIFIER, JToken.JString, JToken.LBRACE, JToken.RPARENT)) {//检查具体语法
			if(tokenIs(JToken.RPARENT)){//@Annotation()
				return anno;
			}else if (tokenIs(JToken.JString)) {//("value")
				anno.setAttribute("value", stringValue());
				this.throwIfNextTokenIsNot(JToken.RPARENT, anno);
				break;
			} else if(tokenIs(JToken.LBRACE)){//@Annotation({})
				anno.setAttribute("value", parseArrayValue());
				this.throwIfNextTokenIsNot(JToken.RPARENT, anno);
				break;
			}else {
				String name = stringValue();
				this.throwIfNoNextToken();
				if(tokenIs(JToken.ASSIGN)){//@Annotation(name=...)
					throwIfNextTokenIsNotOneOf(JToken.JString, JToken.IDENTIFIER, JToken.NUMBER, JToken.LBRACE);
					//不检查具体语法了。。。
					if(tokenIs(JToken.LBRACE)){//{}
						anno.setAttribute(name, parseArrayValue());
						throwIfNoNextToken();
						if(tokenIs(JToken.RPARENT)){
							this.throwIfNoNextToken();
							return anno;
						}else if(tokenIs(JToken.COMMA)){
							continue;
						}else
							throw new JSyntaxException();
					}else{
						String value = stringValue();
						while(throwIfNoNextToken()){
							if(tokenIs(JToken.COMMA)){
								break;
							}
							if(tokenIs(JToken.RPARENT)){
								anno.setAttribute(name, value);
								this.throwIfNoNextToken();
								return anno;
							}
							value += stringValue();
						}
						anno.setAttribute(name, value);
					}
				}else{////@Annotation(ClassName.Field)
					JTokenValueCollection<JToken> tokens = nextAllTokensUntil(JToken.RPARENT);
					anno.setAttribute("value", name+tokens.getValues(""));
					break;
				}
				
			}
		}
		this.throwIfNoNextToken();
		return anno;
	}
	
	protected String parseArrayValue(){
		String value = stringValue();
		while(throwIfNoNextToken()){
			value += stringValue();
			if(tokenIs(JToken.RBRACE)){
				break;
			}else if(tokenIs(JToken.RPARENT)){//error: { )
				throw new JSyntaxException();
			}
		}
		return value;
	}

}
