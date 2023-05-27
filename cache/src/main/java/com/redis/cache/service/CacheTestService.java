package com.redis.cache.service;

import com.redis.cache.domain.Member;
import com.redis.cache.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CacheTestService {

    private final MemberRepository repository;

    @Transactional(readOnly = true)
    @Cacheable(key = "#memberId", value = "memberCacheStore")
    public Member getByMemberId(Long memberId) {
        Member memberPS = repository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("member없음"));
        return memberPS;
    }

    // redis에 value::key 이런식으로 저장되기떄문에, cacheStore가 다르면, 다른 키로 분류되어 다른 캐시 데이터를 갱신할 수있어 주의해야 함
    @CachePut(key = "#memberId", value = "memberCacheStore")
    public Member modifyMember(Long memberId, String name) {
        Member memberPS = repository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("member없음"));
        memberPS.setName(name);
        return memberPS;
    }

    public Member insertMember(String memberName) {
        Member member = new Member();
        member.setName(memberName);
        return repository.save(member);
    }

//    @CacheEvict(value = "memberCacheStore", allEntries = true) // 해당 store의 캐시전부 삭제
    @CacheEvict(value = "memberCacheStore")
    public void deleteMember(Long memberId) {
        Member memberPS = repository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("member없음"));
        repository.delete(memberPS);
    }
}
