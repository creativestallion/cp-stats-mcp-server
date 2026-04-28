package com.example.leetcode_mcp.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CodeforcesClient implements CompetitiveProgrammingClient {

    private final WebClient webClient;

    public CodeforcesClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://codeforces.com/api")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Override
    public String getProfile(final String handle) {
        return webClient.get()
                .uri("/user.info?handles=" + handle)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public String getProblemStats(final String handle) {
        // Fetch submissions and compute accepted unique problems
        return webClient.get()
                .uri("/user.status?handle=" + handle + "&from=1&count=1000")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public String getRecentActivity(final String handle, int limit) {
        if (limit < 1) limit = 10;
        if (limit > 50) limit = 50;
        return webClient.get()
                .uri("/user.status?handle=" + handle + "&from=1&count=" + limit)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public String getContestHistory(final String handle) {
        return webClient.get()
                .uri("/user.rating?handle=" + handle)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}