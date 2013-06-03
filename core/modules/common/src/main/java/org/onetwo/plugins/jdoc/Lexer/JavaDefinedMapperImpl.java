package org.onetwo.plugins.jdoc.Lexer;

import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.jdoc.Lexer.defined.ClassDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.FieldDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.JavaClassDefineImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.MethodDefinedImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.MethodDefinedImpl.MethodParam;
import org.onetwo.plugins.jdoc.data.CodeDoc;
import org.onetwo.plugins.jdoc.data.JClassDoc;
import org.onetwo.plugins.jdoc.data.JFieldDoc;
import org.onetwo.plugins.jdoc.data.RestInterfaceDoc;

public class JavaDefinedMapperImpl implements JavaDefinedMapper<JClassDoc>{

	@Override
	public JClassDoc map(JavaClassDefineImpl jclassDefined) {
		ClassDefinedImpl pubClass = jclassDefined.getPublicClassDefine();
		if(pubClass==null)
			return null;
		JClassDoc jdoc = new JClassDoc();
		jdoc.setName(pubClass.getName());
		jdoc.setDescription(pubClass.getDocument().getDocument());
		
		jdoc.setPackageName(jclassDefined.getPackageDefine().getName());
		
		/*if(jdoc.getFullName().endsWith("LoginController")||jdoc.getFullName().endsWith("ConstantController")){
			System.out.println("test");
		}*/
		
		//fields
		for(FieldDefinedImpl field : pubClass.getFields().values()){
			JFieldDoc fdoc = new JFieldDoc(jdoc);
			fdoc.setName(field.getName());
			fdoc.setTypeName(field.getDeclareType());
			fdoc.setDescription(field.getDocument().getDocument());
			fdoc.setRequired(field.isRequired());
			
			jdoc.addField(fdoc);
		}
		
		String baseUrl = StringUtils.trimToEmpty(pubClass.getAnnotationAttribute("RequestMapping", "value")).replace("\"", "");
		baseUrl = StringUtils.appendStartWith(baseUrl, "/");
		if(baseUrl.equals("/"))
			baseUrl = "";
		//methods
		for(MethodDefinedImpl method : pubClass.getMethods()){
			if(!method.containsAnnotation("RequestMapping"))
				continue;
			RestInterfaceDoc mdoc = new RestInterfaceDoc(jdoc);
			mdoc.setName(method.getName());
			mdoc.setDescription(method.getDocument().getDocument());
			mdoc.getReturnDoc().setType(method.getReturnType());
			mdoc.getReturnDoc().setDataTypes(method.getReturnGenerateTypes());
			
			DocDirectiveInfo rdi = method.getDocument().getDirectiveInfo(DocDirective.RETURN);
			if(rdi!=null){
				mdoc.getReturnDoc().setDescription(rdi.getDescription());
			}

			String restUrl = method.getAnnotationAttribute("RequestMapping", "value", "").replace("\"", "");
			restUrl = baseUrl + StringUtils.appendStartWith(restUrl, "/");
			mdoc.setUrl(restUrl);
			mdoc.setHttpMethod(method.getAnnotationAttribute("RequestMapping", "method", ""));
			
			int pindex = 0;
			for(MethodParam param : method.getParameters()){
				JFieldDoc pdoc = new JFieldDoc(jdoc);
				pdoc.setName(param.getName());
				pdoc.setTypeName(param.getDeclareFullType());
				
				DocDirectiveInfo di = method.getDocument().getDirectiveInfo(DocDirective.PARAM, pindex);
				if(di!=null){
					pdoc.setName(di.getValue(0));
					pdoc.setRequired(LangUtils.tryCastTo(di.getValue(1), boolean.class));
					pdoc.setDescription(di.getValues(2));
				}
				mdoc.addParam(pdoc);
			}
			
			List<DocDirectiveInfo> throwList = method.getDocument().getDirectiveInfos(DocDirective.THROWS);
			for(DocDirectiveInfo di : throwList){
				CodeDoc cdoc = new CodeDoc();
				cdoc.setCode(di.getValue(0));
				cdoc.setMessage(di.getValue(1));
				cdoc.setMemo(di.getValue(2));
				mdoc.addErrorCode(cdoc);
			}
			
			jdoc.addMethod(mdoc);
		}
		return jdoc;
	}

}
