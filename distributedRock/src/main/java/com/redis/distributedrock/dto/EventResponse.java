package com.redis.distributedrock.dto;

import lombok.Getter;

@Getter
public class EventResponse {
    private Long id;
    private Long ticketLimit;

    public EventResponse(Long id, Long ticketLimit) {
        this.id = id;
        this.ticketLimit = ticketLimit;
    }
}
