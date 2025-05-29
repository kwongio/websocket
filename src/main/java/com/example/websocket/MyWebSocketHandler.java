package com.example.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MyWebSocketHandler extends WebSocketMessageB {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserIdFromSession(session);  // <- 추출
        session.getAttributes().put("userId", userId); // 필요 시 저장
        sessions.add(session);
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen()) {
                webSocketSession.sendMessage(new TextMessage("새로운 클라이언트가 연결되었습니다: " + session.getId()));
            }
        }
        System.out.println("웹소켓 연결됨: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen()) {
                webSocketSession.sendMessage(new TextMessage("서버로부터 받은 메시지: " + message.getPayload()));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("웹소켓 연결 종료: " + session.getId());
    }
    //const ws= new WebSocket("ws://localhost:8080/ws")
    //ws.onmessage = message => console.log(`${message.data}`);
    //ws.send("hello");

    private String getUserIdFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery(); // e.g., "userId=user123"
        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length == 2 && "userId".equals(pair[0])) {
                    return pair[1];
                }
            }
        }
        return "알 수 없음";
    }
}
