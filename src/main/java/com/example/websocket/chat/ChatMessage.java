package com.example.websocket.chat;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class ChatMessage implements Serializable {
    private String roomId;
    private String sender;
    private String message;
}
