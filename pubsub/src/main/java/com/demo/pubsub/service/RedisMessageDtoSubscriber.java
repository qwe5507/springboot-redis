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
public class RedisMessageDtoSubscriber implements MessageListener {

    private static List<CoffeeDto> coffeeDtos = new ArrayList<>();
    private ObjectMapper om = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            CoffeeDto coffeeDto = om.readValue(message.getBody(), CoffeeDto.class);
            coffeeDtos.add(coffeeDto);

            log.info("Dto Message received: {}", message.toString());
            log.info("Total CoffeeDtp Size: {}", coffeeDtos.size());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
