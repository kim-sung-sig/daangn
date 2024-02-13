package kr.ezen.daangn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// stomp 엔드포인트를 등록
		registry.addEndpoint("/ws").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 클라이언트로부터 메시지를 받을 때, 해당 메시지를 처리할 엔드포인트를 설정합니다.
        registry.setApplicationDestinationPrefixes("/app");
        // 클라이언트에게 메시지를 보낼 때 사용할 prefix를 설정합니다.
        registry.enableSimpleBroker("/topic");
	}
	
}
