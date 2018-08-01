FreeMarker template error (DEBUG mode; use RETHROW in production!):
The following has evaluated to null or missing:
==> _globalConfig.getModuleName()  [in template "META-INF/dbgenerator/webadmin/Entity.java.ftl" at line 1, column 26]

----
Tip: If the failing expression is known to legally refer to something that's sometimes null or missing, either specify a default value like myOptionalVar!myDefault, or use <#if myOptionalVar??>when-present<#else>when-missing</#if>. (These only cover the last step of the expression; to cover the whole expression, use parenthesis: (myOptionalVar.foo)!myDefault, (myOptionalVar.foo)??
----

----
FTL stack trace ("~" means nesting-related):
	- Failed at: #assign requestPath = "/${_globalConf...  [in template "META-INF/dbgenerator/webadmin/Entity.java.ftl" at line 1, column 1]
----

Java stack trace (for programmers):
----
freemarker.core.InvalidReferenceException: [... Exception message was already printed; see it above ...]
	at freemarker.core.InvalidReferenceException.getInstance(InvalidReferenceException.java:134)
	at freemarker.core.EvalUtil.coerceModelToTextualCommon(EvalUtil.java:467)
	at freemarker.core.EvalUtil.coerceModelToStringOrMarkup(EvalUtil.java:389)
	at freemarker.core.EvalUtil.coerceModelToStringOrMarkup(EvalUtil.java:358)
	at freemarker.core.DollarVariable.calculateInterpolatedStringOrMarkup(DollarVariable.java:96)
	at freemarker.core.StringLiteral._eval(StringLiteral.java:97)
	at freemarker.core.Expression.eval(Expression.java:83)
	at freemarker.core.Assignment.accept(Assignment.java:134)
	at freemarker.core.Environment.visit(Environment.java:325)
	at freemarker.core.Environment.visit(Environment.java:331)
	at freemarker.core.Environment.process(Environment.java:304)
	at freemarker.template.Template.process(Template.java:382)
	at org.onetwo.common.db.generator.ftl.FtlEngine.generateFile(FtlEngine.java:67)
	at org.onetwo.common.db.generator.DbGenerator$DbTableGenerator.lambda$6(DbGenerator.java:364)
	at org.onetwo.common.db.generator.DbGenerator$DbTableGenerator$$Lambda$11/1796047085.accept(Unknown Source)
	at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1374)
	at java.util.stream.ReferencePipeline$Head.forEach(ReferencePipeline.java:580)
	at org.onetwo.common.db.generator.DbGenerator$DbTableGenerator.generate(DbGenerator.java:352)
	at org.onetwo.common.db.generator.DbGenerator$DbTableGenerator.access$0(DbGenerator.java:343)
	at org.onetwo.common.db.generator.DbGenerator.lambda$4(DbGenerator.java:149)
	at org.onetwo.common.db.generator.DbGenerator$$Lambda$10/1371376476.accept(Unknown Source)
	at java.util.ArrayList.forEach(ArrayList.java:1249)
	at org.onetwo.common.db.generator.DbGenerator.generate(DbGenerator.java:148)
	at org.onetwo.common.db.generator.DbmGenerator.generate(DbmGenerator.java:125)
	at org.onetwo.common.db.generator.DbmGenerator.generate(DbmGenerator.java:117)
	at org.onetwo.boot.module.swagger.SwaggerEntityDbmGeneratorTest.generateCode(SwaggerEntityDbmGeneratorTest.java:24)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:483)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:50)
	at org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:38)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:459)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:675)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:382)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:192)
