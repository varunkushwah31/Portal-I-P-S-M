package com.academia.placement.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * IP-based token bucket rate limiter protecting API endpoints against brute-force and volumetric abuse.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS_PER_MINUTE = 200;
    private final Map<String, RequestCounter> clientCounters = new ConcurrentHashMap<>();

    private static class RequestCounter {
        private final long windowStart;
        private final AtomicInteger count;

        RequestCounter(long windowStart) {
            this.windowStart = windowStart;
            this.count = new AtomicInteger(1);
        }

        boolean incrementAndCheckLimit(long currentWindow) {
            if (currentWindow != windowStart) {
                return false;
            }
            return count.incrementAndGet() > MAX_REQUESTS_PER_MINUTE;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        long currentMinuteWindow = System.currentTimeMillis() / 60000;

        RequestCounter counter = clientCounters.compute(clientIp, (ip, existing) -> {
            if (existing == null || existing.windowStart != currentMinuteWindow) {
                return new RequestCounter(currentMinuteWindow);
            }
            return existing;
        });

        if (counter.count.get() > MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":429,\"error\":\"Too Many Requests\",\"message\":\"Rate limit exceeded. Please retry after one minute.\"}");
            return;
        }

        counter.count.incrementAndGet();
        filterChain.doFilter(request, response);
    }
}
