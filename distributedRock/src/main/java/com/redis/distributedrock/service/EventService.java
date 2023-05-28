package com.redis.distributedrock.service;

import com.redis.distributedrock.domain.Event;
import com.redis.distributedrock.domain.EventRepository;
import com.redis.distributedrock.domain.EventTicket;
import com.redis.distributedrock.domain.EventTicketRepository;
import com.redis.distributedrock.dto.EventResponse;
import com.redis.distributedrock.dto.EventTicketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventTicketRepository eventTicketRepository;

    @Transactional
    public EventResponse createEvent(final Long ticketLimit) {
        Event savedEvent = eventRepository.save(new Event(ticketLimit));
        return new EventResponse(savedEvent.getId(), savedEvent.getTicketLimit());
    }

    @Transactional
    public EventTicketResponse createEventTicket(final Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (event.isClosed()) {
            throw new RuntimeException("마감 되었습니다.");
        }

        EventTicket savedEventTicket = eventTicketRepository.save(new EventTicket(event));
        return new EventTicketResponse(savedEventTicket.getId(), savedEventTicket.getEvent().getId());
    }

}
