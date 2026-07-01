package com.atous.auth.application.port.out;

public interface RateLimiterPort { boolean isAllowed(String key); void recordFailure(String key); void reset(String key); }
