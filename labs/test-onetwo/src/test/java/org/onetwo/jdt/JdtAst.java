package org.onetwo.jdt;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class JdtAst {
	private ASTParser astParser = ASTParser.newParser(AST.JLS3);//非常慢  
    
    /** 
     * 获得java源文件的结构CompilationUnit 
     * @param javaFilePath java文件的绝对路径 
     * @return CompilationUnit 
     * @throws Exception 
     */  
    public CompilationUnit getCompilationUnit(String javaFilePath) throws Exception {  
          
        BufferedInputStream bufferedInputStream = new BufferedInputStream(  
                new FileInputStream(javaFilePath));  
        byte[] input = new byte[bufferedInputStream.available()];  
        bufferedInputStream.read(input);  
        bufferedInputStream.close();  
        this.astParser.setSource(new String(input).toCharArray());  
        /**/  
        CompilationUnit result = (CompilationUnit) (this.astParser.createAST(null));//很慢  
          
        return result;  
//        List commentList = result.getCommentList();  
//        PackageDeclaration package1 = result.getPackage();  
//        List importList = result.imports();  
//        TypeDeclaration type = (TypeDeclaration) result.types().get(0);  
//        FieldDeclaration[] fieldList = type.getFields();  
//        MethodDeclaration[] methodList = type.getMethods();  
//        Block method_block=methodList[1].getBody();  
//        TryStatement try_stmt=(TryStatement)method_block.statements().get(0);  
//        ForStatement for_stmt=(ForStatement)try_stmt.getBody().statements().get(0);  
//        ExpressionStatement express_stmt=(ExpressionStatement) ((Block)for_stmt.getBody()).statements().get(0);  
         
    }  
}
