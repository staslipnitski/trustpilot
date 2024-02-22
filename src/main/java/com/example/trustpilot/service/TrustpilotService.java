package com.example.trustpilot.service;

import com.example.trustpilot.entity.ReviewResponse;
import com.example.trustpilot.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TrustpilotService implements ITrustpilotService {

    @Value("${trustpilot.url}")
    private String trustpilotUrl;
    private final WebClient webClient;

    private static final String RATING_ATTRIBUTE = "data-rating-typography";
    private static final String REVIEW_COUNT_ATTRIBUTE = "data-reviews-count-typography";

    @Override
    public Mono<ReviewResponse> getReviews(String domain) {
        return webClient.get()
                .uri(trustpilotUrl + "review/{domain}", domain)
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        response -> Mono.error(new ResourceNotFoundException("The domain " + domain + " was not found."))
                )
                .bodyToMono(String.class)
                .map(this::parseHtml);
    }

    private ReviewResponse parseHtml(String html) {
        Document document = Jsoup.parse(html);
        ReviewResponse response = new ReviewResponse();

        String rating = document.getElementsByAttribute(RATING_ATTRIBUTE).text();
        if (NumberUtils.isCreatable(rating)) {
            response.setRating(Double.valueOf(rating));
        }

        String reviewCount = document.getElementsByAttribute(REVIEW_COUNT_ATTRIBUTE).text();
        response.setReviewsCount(parseReviewsCountToInt(reviewCount));

        return response;
    }

    public static Integer parseReviewsCountToInt(String input) {
        // Regular expression to find numbers with commas
        String numberRegex = "\\d{1,3}(,\\d{3})*";

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(numberRegex);
        java.util.regex.Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String numberStr = matcher.group().replace(",", "");
            if (NumberUtils.isCreatable(numberStr)){
                return Integer.parseInt(numberStr);
            }
        }

        //handle not parsable value
        return null;
    }
}
