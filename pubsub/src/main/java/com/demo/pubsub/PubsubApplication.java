package com.demo.pubsub;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;


@SpringBootApplication
@RequiredArgsConstructor
public class PubsubApplication implements CommandLineRunner {
    private final RedisTemplate<String, String> stringValueRedisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(PubsubApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // String 메시지 발행
        stringValueRedisTemplate.convertAndSend("ch01", "publish test!");
    }
}
