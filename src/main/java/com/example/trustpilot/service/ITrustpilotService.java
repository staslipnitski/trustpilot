package com.example.trustpilot.service;

import com.example.trustpilot.entity.ReviewResponse;
import reactor.core.publisher.Mono;

public interface ITrustpilotService {
    Mono<ReviewResponse> getReviews(String domain);
}
