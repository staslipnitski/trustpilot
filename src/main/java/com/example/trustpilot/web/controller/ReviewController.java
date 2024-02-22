package com.example.trustpilot.web.controller;

import com.example.trustpilot.service.TrustpilotService;
import com.example.trustpilot.entity.ReviewResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class ReviewController {

    private TrustpilotService trustpilotService;

    @GetMapping("/reviews/{domain}")
    public Mono<ResponseEntity<ReviewResponse>> getExternalResource(@PathVariable String domain) {
        return trustpilotService.getReviews(domain)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
