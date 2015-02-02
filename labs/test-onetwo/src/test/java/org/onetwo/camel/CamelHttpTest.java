package org.onetwo.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;

public class CamelHttpTest {
	
//	@Test
	public void test() throws Exception{
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new ActivemqRouter());
        
        ProducerTemplate pt = context.createProducerTemplate();
        context.start();
        
        pt.sendBody("direct:start", null);
        
        LangUtils.await(10);
        context.stop();
	}
	
	
	public static void main(String[] args) throws Exception{
		Main main = new Main();
        main.addRouteBuilder(new ActivemqRouter());
        main.enableHangupSupport();
        main.run();
	}

}
