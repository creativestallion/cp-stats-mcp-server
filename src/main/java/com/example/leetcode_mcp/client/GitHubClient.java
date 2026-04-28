package com.example.leetcode_mcp.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.Executors;

@Component
public class GitHubClient implements PlatformClient {

    private final WebClient webClient;

    public GitHubClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }

    public String getProfile(final String username) {
        return webClient.get()
                .uri("/users/" + username)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn("{\"error\": \"User not found: " + username + "\"}")
                .block();
    }

    @Override
    public String getRecentActivity(final String handle, int limit) {
        if (limit < 1) limit = 10;
        if (limit > 30) limit = 30;
        return webClient.get()
                .uri("/users/" + handle + "/events/public?per_page=" + limit)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn("{\"error\": \"User not found: " + handle + "\"}")
                .block();
    }

    public String getRepositories(final String username) {
        return webClient.get()
                .uri("/users/" + username + "/repos?sort=stars&per_page=10")
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn("{\"error\": \"User not found: " + username + "\"}")
                .block();
    }

    public List<String> getProfilesInParallel(final List<String> usernames) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = usernames.stream()
                    .map(u -> executor.submit(() -> getProfile(u)))
                    .toList();
            return futures.stream()
                    .map(f -> {
                        try {
                            return f.get();
                        } catch (Exception e) {
                            return "Error fetching: " + e.getMessage();
                        }
                    })
                    .toList();
        }
    }

    public List<String> getRepositoriesInParallel(final List<String> usernames) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = usernames.stream()
                    .map(u -> executor.submit(() -> getRepositories(u)))
                    .toList();
            return futures.stream()
                    .map(f -> {
                        try {
                            return f.get();
                        } catch (Exception e) {
                            return "Error fetching: " + e.getMessage();
                        }
                    })
                    .toList();
        }
    }
}