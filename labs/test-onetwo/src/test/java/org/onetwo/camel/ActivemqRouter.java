package org.onetwo.camel;

import org.apache.camel.builder.RouteBuilder;

public class ActivemqRouter extends RouteBuilder {
	
	public static class UrlBean {
		public String getReportUrl(){
			return "outer/cardQuery/status.json?idcard=441827198403057750";
		}
	}

	@Override
	public void configure() throws Exception {
//		String brokerURL = "tcp://localhost:61616";
//		getContext().addComponent("activemq", ActiveMQComponent.activeMQComponent(brokerURL));
//		getContext().addComponent("http", HttpComponent.);
		
//		from("activemq:queue:web.operation")
		System.out.println("activemq~~~");
//		from("direct:start")
		from("timer://start")
			.bean(UrlBean.class, "getReportUrl")
			.recipientList(simple("http://localhost:8080/iccard-web/${body}"))
			.to("file:D:/camel/test?fileName=test");
		System.out.println("activemq end~~~");
	}
	
	

}
