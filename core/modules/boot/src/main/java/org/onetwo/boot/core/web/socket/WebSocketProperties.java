package org.onetwo.boot.core.web.socket;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/***
 * 首先使用 SockJS 建立连接
	var socket = new SockJS(StompProps.endpoint);
	stompClient = Stomp.over(socket);
	
	// 连接成功回调
    stompClient.connect({}, function (frame) {
        // 进行页面设置
        setConnected(true);
        // 订阅服务端发送回来的消息: /topic/greetings
        stompClient.subscribe(BrokerProps.simplePrefixes+'/receive', function (greeting) {
            // 将服务端发送回来的消息展示出来
            showGreeting(JSON.parse(greeting.body));
        });
    });
            
    // send(/app/sendAll)，controller收到后通过messageSendingOperations.convertAndSend(BrokerProps.simplePrefixes+"/receive");
	stompClient.send(BrokerProps.appPrefixes + "/sendAll")
 * @author way
 *
 */
@Data
@ConfigurationProperties(WebSocketProperties.PREFIX)
public class WebSocketProperties {
	public static final String PREFIX = BootJFishConfig.PREFIX + ".websocket";
	/**
	 * jfish.websocket.enabled: true
	 */
	public static final String ENABLED_KEY = PREFIX + ".enabled";

	StompProps stomp = new StompProps();
	
	BrokerProps broker = new BrokerProps();

	@Data
	public static class StompProps {
		/***
		 * 
		 */
		String[] endpoints = new String[] {"/stomp"};
		String[] allowedOrigins;
	}

	@Data
	public static class BrokerProps {
		String userPrefix = "/user";
		/***
		 * simple broker destinationPrefixes
		 * 设置消息代理的前缀，如果消息的前缀为"/broadcast"，就会将消息转发给消息代理（broker）
	     * 再由消息代理广播给当前连接的客户端
		 */
		String[] simplePrefixes = new String[] { "/broadcast"};
		
		/***
		 * applicationDestinationPrefixes
		 * 下面方法可以配置一个或多个前缀，通过这些前缀过滤出需要被注解方法处理的消息。
	     * 例如这里表示前缀为"/public"的destination可以通过@MessageMapping注解的方法处理
	     * 而其他 destination（例如"/topic""/queue"）将被直接交给 broker 处理
		 */
		String[] appPrefixes = new String[] { "/app" };
	}
	
}
