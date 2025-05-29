package com.example.websocket.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChannelTopic topic;
    private final RedisPublisher redisPublisher;

    @MessageMapping("/chat") // /pub/chat
    public void sendMessage(ChatMessage message) {
        redisPublisher.publish(topic, message);
    }
}
