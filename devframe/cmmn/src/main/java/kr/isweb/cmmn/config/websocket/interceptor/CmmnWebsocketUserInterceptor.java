package kr.isweb.cmmn.config.websocket.interceptor;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import kr.isweb.cmmn.config.websocket.CmmnWebsocketUser;

public class CmmnWebsocketUserInterceptor implements ChannelInterceptor {
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		if(StompCommand.CONNECT.equals(accessor.getCommand())) {
			Object row = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
			if(row instanceof Map) {
				Object name = ((Map<?, ?>) row).get("username");
				if(name instanceof ArrayList) {
					 accessor.setUser(new CmmnWebsocketUser(((ArrayList<?>) name).get(0).toString()));
				}
			}
		}
		return message;
	}
}
