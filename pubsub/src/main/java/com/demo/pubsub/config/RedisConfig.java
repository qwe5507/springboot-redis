package com.demo.pubsub.config;

import com.demo.pubsub.dto.CoffeeDto;
import com.demo.pubsub.service.RedisMessageDtoSubscriber;
import com.demo.pubsub.service.RedisMessageStringSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, String> stringValueRedisTemplate() {

        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, CoffeeDto> dtoValueRedisTemplate() {

        RedisTemplate<String, CoffeeDto> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(CoffeeDto.class));
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageStringListener(), topic01());
        container.addMessageListener(messageDtoListener(), topic02());

        return container;
    }

    @Bean
    MessageListenerAdapter messageDtoListener() {
        return new MessageListenerAdapter(new RedisMessageDtoSubscriber());
    }

    @Bean
    MessageListenerAdapter messageStringListener() {
        return new MessageListenerAdapter(new RedisMessageStringSubscriber());
    }

    @Bean
    ChannelTopic topic01() {
        return new ChannelTopic("ch01");
    }

    @Bean
    ChannelTopic topic02() {
        return new ChannelTopic("ch02");
    }
}
