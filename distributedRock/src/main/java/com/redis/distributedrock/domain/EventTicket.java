package com.redis.distributedrock.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity

public class EventTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    public EventTicket(Event event) {
        this.event = event;
    }

    public EventTicket() {
    }
}