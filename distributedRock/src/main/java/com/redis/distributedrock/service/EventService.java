package com.redis.distributedrock.service;

import com.redis.distributedrock.domain.Event;
import com.redis.distributedrock.domain.EventRepository;
import com.redis.distributedrock.domain.EventTicket;
import com.redis.distributedrock.domain.EventTicketRepository;
import com.redis.distributedrock.dto.EventResponse;
import com.redis.distributedrock.dto.EventTicketResponse;
import com.redis.distributedrock.redis.RedisLockRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventTicketRepository eventTicketRepository;
    private final RedisLockRepository redisLockRepository;
    private final RedissonClient redissonClient;

    @Transactional
    public EventResponse createEvent(final Long ticketLimit) {
        Event savedEvent = eventRepository.save(new Event(ticketLimit));
        return new EventResponse(savedEvent.getId(), savedEvent.getTicketLimit());
    }

    /*
        Reddison라이브러리를 활용한 Pub/Sub기반의 분산 Lock
     */
    @Transactional
    public EventTicketResponse createEventTicketByReddison(final Long eventId) {
        RLock lock = redissonClient.getLock(String.valueOf(eventId));

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                throw new RuntimeException("Lock을 획득하지 못했습니다.");
            }

            /* 비즈니스 로직 */
            Event event = eventRepository.findById(eventId).orElseThrow();
            if (event.isClosed()) {
                throw new RuntimeException("마감 되었습니다.");
            }

            EventTicket savedEventTicket = eventTicketRepository.save(new EventTicket(event));
            return new EventTicketResponse(savedEventTicket.getId(), savedEventTicket.getEvent().getId());
            /* 비즈니스 로직 */
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    // Spring starter Redis의 Default라이브러리인 Lettuce를 이용한 Spin Lock 활용
    @Transactional
    public EventTicketResponse createEventTicketByLettuce(final Long eventId) throws InterruptedException {
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
