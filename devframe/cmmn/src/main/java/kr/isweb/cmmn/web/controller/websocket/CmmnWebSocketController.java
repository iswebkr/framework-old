package kr.isweb.cmmn.web.controller.websocket;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CmmnWebSocketController {

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/private")
	public void sendToPrivateMessage(MessageHeaders messageHeaders, @Payload String message, @Header(name="simpSessionId") String sessionId, Principal principal) {
		simpMessagingTemplate.convertAndSendToUser(sessionId, "/topic/message", message, messageHeaders);
	}

	@MessageMapping("/public")
	public void notificationToPublic(MessageHeaders messageHeaders, @Payload String message, @Header(name="simpSessionId") String sessionId) {
		simpMessagingTemplate.convertAndSend("/topic/message", message, messageHeaders);
	}
}
