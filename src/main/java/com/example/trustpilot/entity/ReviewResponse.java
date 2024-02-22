package com.example.trustpilot.entity;

import lombok.Data;

@Data
public class ReviewResponse {
    private Integer reviewsCount;
    private Double rating;
}
