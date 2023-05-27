package com.redis.cache.controller;

import com.redis.cache.domain.Member;
import com.redis.cache.service.CacheTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class CacheTestController {

    private final CacheTestService service;

    @GetMapping("/{memberId}")
    public Member getMember(@PathVariable Long memberId) {
        Member member = service.getByMemberId(memberId);
        return member;
    }

    @PutMapping("/{memberId}")
    public Member insertMember(@PathVariable Long memberId, @RequestParam String name) {
        return service.modifyMember(memberId, name);
    }

    @PostMapping("/{memberName}")
    public Member insertMember(@PathVariable String memberName) {
        return service.insertMember(memberName);
    }

    @DeleteMapping("/{memberId}")
    public String insertMember(@PathVariable Long memberId) {
        service.deleteMember(memberId);
        return "success";
    }
}
