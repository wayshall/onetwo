package org.onetwo.boot.module.qlexpress;
/**
 * @author weishao zeng
 * <br/>
 */

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;

import com.beust.jcommander.internal.Maps;
import com.ql.util.express.ArraySwap;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import com.ql.util.express.InstructionSet;
import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.Operator;
import com.ql.util.express.config.QLExpressRunStrategy;
import com.ql.util.express.exception.QLException;
import com.ql.util.express.instruction.op.OperatorBase;

import lombok.Data;

public class ExpressExecutorTest {

	QLExpressProperties props = new QLExpressProperties();
	ExpressRunner runner = new ExpressRunner();
	ExpressExecutor executor = new ExpressExecutor(runner, props);
	
	@Test
	public void test() {
		String exp = "return new Date()";
		Object res = executor.execute(exp, Maps.newHashMap());
		assertThat(res).isInstanceOf(Date.class);
	}
	
	@Test
	public void testGerVars() {
		props.setTrace(true);
		String exp = "(客户新品订单数==null || 客户所在地区订单数==null || 客户所在地区订单数==0)?0:(客户新品订单数/客户所在地区订单数)";
		String[] vars = executor.getVars(exp);
		System.out.println("vars: " + LangUtils.toString(vars));
		
		exp = "if (客户新品订单数==null || 客户所在地区订单数==null || 客户所在地区订单数==0){" +
				 "return 0;" +
			  "} else {" + 
				 "double res = 客户新品订单数/客户所在地区订单数;" +
				 "if (res>1) {" +
				 	"return 1;" + 
				 "} else {" + 
				 	"return res;" + 
				 "}" + 
			  "}";
		vars = executor.getVars(exp);
		System.out.println("vars: " + LangUtils.toString(vars));
		Map<String,Object> context = CUtils.asMap("客户新品订单数", 3.0, "客户所在地区订单数", 6.0);
		Object res = executor.execute(exp, context);
		System.out.println("res: " + res);
		assertThat(res).isEqualTo(0.5);
	}

	@Test
	public void testForbiddenInvokeSecurityRiskMethods() throws Exception {
	    QLExpressRunStrategy.setForbiddenInvokeSecurityRiskMethods(true);
	    DefaultContext<String, Object> context = new DefaultContext<String, Object>();
	    try {
	    	String express = "System.exit(1);";
	    	Object r = runner.execute(express, context, null, true, false);
	    	System.out.println(r);
	    	throw new Exception("没有捕获到不安全的方法");
	    } catch (QLException e) {
	    	System.out.println(e);
	    }
	}
	
	@Test
	public void testCheckSyntax() throws Exception {
		ExpressRunner runner = new ExpressRunner();
		String express = "月销量>10";
		boolean r = runner.checkSyntax(express);
		assertThat(r).isEqualTo(true);

		express = "用户.id==10";
		InstructionSet iset = runner.parseInstructionSet(express);
		System.out.println("iset: " + iset);
		

		DefaultContext<String, Object> context = new DefaultContext<String, Object>();
		context.put("用户", 110);
		express = "用户.id==10";
		r = (boolean)runner.execute(express, context, null, true, true);
		assertThat(r).isFalse();
	}
	
	@Test
	public void testSimple() throws Exception {
		ExpressRunner runner = new ExpressRunner();
		DefaultContext<String, Object> context = new DefaultContext<String, Object>();
		context.put("a",1);
		context.put("b",2);
		context.put("c",3);
		String express = "a+b*c";
		Object r = runner.execute(express, context, null, true, false);
		System.out.println(r);
		
		CustomerTestData cust = new CustomerTestData();
		cust.setCustId("test");
		context = new DefaultContext<String, Object>();
		context.put("customer", cust);
		express = "customer.custId=='test'";
		r = runner.execute(express, context, null, true, false);
		System.out.println(r);
		r = runner.execute("7.5", context, null, true, false);
		System.out.println(r);
		

		System.out.println("=========test");
		runner.parseInstructionSet("for(){");
		List<String> errorList = new ArrayList<String>();
		r = runner.execute("test", context, errorList, true, true);
		System.out.println(r);
		System.out.println(errorList);
	}
    
    @Test
    public void testOperatorContextPut() throws Exception{
        ExpressRunner runner = new ExpressRunner();
        OperatorBase op = new OperatorContextPut("contextPut");
        runner.addFunction("contextPut", op);
        String exp = "contextPut('success','false');contextPut('error','错误信息');contextPut('warning','提醒信息')";
        IExpressContext<String, Object> context = new DefaultContext<String, Object>();
        // success会被contextPut('success','false')覆盖
        context.put("success","true");
        Object result = runner.execute(exp,context,null,false,true);
        System.out.println(context);
//        assertThat((boolean)).isTrue();
    }
	
	@Test
	public void testJoinOperator() throws Exception {
		ExpressRunner runner = new ExpressRunner(true, true);
		DefaultContext<String, Object> context = new DefaultContext<String, Object>();
		runner.addFunction("join",new JoinOperator());
		Object r = runner.execute("join(1,2,3)", context, null, false, true);
		System.out.println("testJoinOperator1: " + r);
		
		runner.addOperator("join2", new JoinOperator());
		r = runner.execute("1 join2 2", context, null, false, true);
		System.out.println("testJoinOperator2: " + r);
	}
	

	
	@Test
	public void testSystemFunc() throws Exception {
		ExpressRunner runner = new ExpressRunner(true, true);
		DefaultContext<String, Object> context = new DefaultContext<String, Object>();
		context.put("name", "我叫什么名字？");
		Object res = runner.execute("name like \"我叫什么%\"", context, null, false, true);
		System.out.println("res: " + res);
		assertThat((boolean)res).isTrue();
		
		runner.execute("name like '我叫什么名字？%'", context, null, false, true);
		assertThat((boolean)res).isTrue();
	}
	
	
	class OperatorContextPut extends OperatorBase {
        
        public OperatorContextPut(String aName) {
            this.name = aName;
        }
    
        @Override
        public OperateData executeInner(InstructionSetContext parent, ArraySwap list) throws Exception {
            String key = list.get(0).toString();
            Object value = list.get(1);
            parent.put(key,value);
            return null;
        }
    }
	
	@Data
	public static class CustomerTestData {
		String custId;
	}
	
	public class JoinOperator extends Operator{
		public Object executeInner(Object[] list) throws Exception {
			Object opdata1 = list[0];
			Object opdata2 = list[1];
			if(opdata1 instanceof java.util.List){
				((java.util.List)opdata1).add(opdata2);
				return opdata1;
			}else{
				java.util.List result = new java.util.ArrayList();
				result.add(opdata1);
				result.add(opdata2);
				return result;				
			}
		}
	}

}
