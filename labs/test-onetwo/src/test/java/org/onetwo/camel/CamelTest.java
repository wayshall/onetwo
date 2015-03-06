package org.onetwo.camel;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class CamelTest extends Main {
	
	public static void main(String[] args) throws Exception{
		
		Main main = new Main();
        main.addRouteBuilder(new ActivemqRouter());
        main.enableHangupSupport();
        main.run();
	}

}
