package com.demo.pubsub.service;

import com.demo.pubsub.dto.CoffeeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RedisMessageStringSubscriber implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("String Message received: {}", message.toString());
    }
}
