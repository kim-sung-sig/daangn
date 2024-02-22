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
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 메시지를 보낼때
		registry.enableSimpleBroker("/sub");
		// 메시지를 받을 때
		registry.setApplicationDestinationPrefixes("/pub");
	}
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// stomp 엔드포인트를 등록
		registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
		// 우선 은 모든곳에서 할수 잇게 하겟다 왜냐 주소가없거든..
	}

}
