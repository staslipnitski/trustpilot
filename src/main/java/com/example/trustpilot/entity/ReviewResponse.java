package com.example.trustpilot.service.dto;

import lombok.Data;

@Data
public class ReviewResponse {
    private Integer reviewsCount;
    private Double rating;
}
