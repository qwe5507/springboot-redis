package com.redis.distributedrock.dto;

import lombok.Getter;

@Getter
public class EventTicketResponse {
    private Long id;
    private Long eventId;

    public EventTicketResponse(Long id, Long eventId) {
        this.id = id;
        this.eventId = eventId;
    }
}
