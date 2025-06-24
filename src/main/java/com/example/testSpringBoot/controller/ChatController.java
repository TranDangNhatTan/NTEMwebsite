package com.example.testSpringBoot.controller;

import com.example.testSpringBoot.model.Message;
import com.example.testSpringBoot.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageRepository messageRepository; // Sử dụng repository bạn đã có

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message chatMessage) {
        chatMessage.setSentAt(new Timestamp(System.currentTimeMillis()));

        // Lưu tin nhắn vào database
        Message savedMessage = messageRepository.save(chatMessage);

        // Gửi tin nhắn (đã có ID và timestamp) đến người nhận
        simpMessagingTemplate.convertAndSendToUser(
                String.valueOf(savedMessage.getReceiverId()),
                "/queue/messages",
                savedMessage
        );
    }
}