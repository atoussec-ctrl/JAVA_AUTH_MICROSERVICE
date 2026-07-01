package com.atous.auth.infrastructure.redis;

import com.atous.auth.application.port.out.RateLimiterPort; import org.springframework.stereotype.Component; import java.util.concurrent.*; import java.util.concurrent.atomic.AtomicInteger;

@Component public class InMemoryRateLimiterAdapter implements RateLimiterPort { private final ConcurrentHashMap<String, AtomicInteger> failures=new ConcurrentHashMap<>(); public boolean isAllowed(String k){return failures.getOrDefault(k,new AtomicInteger()).get()<10;} public void recordFailure(String k){failures.computeIfAbsent(k, x->new AtomicInteger()).incrementAndGet();} public void reset(String k){failures.remove(k);} }
