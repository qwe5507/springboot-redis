package com.redis.distributedrock.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ticketLimit;

    @OneToMany(mappedBy = "event")
    private List<EventTicket> eventTickets;

    public Event(Long ticketLimit) {
        this.ticketLimit = ticketLimit;
    }

    public boolean isClosed() {
        return eventTickets.size() >= ticketLimit;
    }
}