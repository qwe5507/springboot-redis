package com.redis.distributedrock.service;

import com.redis.distributedrock.domain.Event;
import com.redis.distributedrock.domain.EventRepository;
import com.redis.distributedrock.domain.EventTicket;
import com.redis.distributedrock.domain.EventTicketRepository;
import com.redis.distributedrock.dto.EventResponse;
import com.redis.distributedrock.dto.EventTicketResponse;
import com.redis.distributedrock.redis.RedisLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventTicketRepository eventTicketRepository;
    private final RedisLockRepository redisLockRepository;

    @Transactional
    public EventResponse createEvent(final Long ticketLimit) {
        Event savedEvent = eventRepository.save(new Event(ticketLimit));
        return new EventResponse(savedEvent.getId(), savedEvent.getTicketLimit());
    }

    @Transactional
    public EventTicketResponse createEventTicket(final Long eventId) throws InterruptedException {
        while (!redisLockRepository.lock(eventId)) {
            Thread.sleep(100);
        } // 락을 획득하기 위해 대기

        try {
            Event event = eventRepository.findById(eventId).orElseThrow();
            if (event.isClosed()) {
                throw new RuntimeException("마감 되었습니다.");
            }

            EventTicket savedEventTicket = eventTicketRepository.save(new EventTicket(event));
            return new EventTicketResponse(savedEventTicket.getId(), savedEventTicket.getEvent().getId());
        } finally {
            redisLockRepository.unlock(eventId);
            // 락 해제
        }
    }

}
