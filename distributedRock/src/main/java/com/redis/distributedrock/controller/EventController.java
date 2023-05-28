package com.redis.distributedrock.controller;

import com.redis.distributedrock.dto.EventRequest;
import com.redis.distributedrock.dto.EventResponse;
import com.redis.distributedrock.dto.EventTicketResponse;
import com.redis.distributedrock.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest request) {
        Long ticketLimit = request.getTicketLimit();
        EventResponse response = eventService.createEvent(ticketLimit);

        return ResponseEntity
                .created(URI.create("/events/" + response.getId()))
                .body(response);
    }

    @PostMapping("/{eventId}/tickets")
    public ResponseEntity<EventTicketResponse> createEventTicket(@PathVariable final Long eventId) throws Exception {
        EventTicketResponse response = eventService.createEventTicket(eventId);

        return ResponseEntity
                .created(URI.create("/events/" + response.getEventId() + "/" + response.getId()))
                .body(response);
    }
}