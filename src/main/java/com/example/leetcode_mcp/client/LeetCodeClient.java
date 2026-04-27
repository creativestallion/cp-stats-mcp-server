package com.example.leetcode_mcp.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.Executors;

@Component
public class LeetCodeClient {

    private final WebClient webClient;

    public LeetCodeClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://leetcode.com/graphql")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Referer", "https://leetcode.com")
                .build();
    }

    public String getUserProfile(final String username) {
        var query = """
                {
                  matchedUser(username: "%s") {
                    username
                    profile {
                      realName
                      ranking
                      reputation
                      starRating
                    }
                    submitStats {
                      acSubmissionNum {
                        difficulty
                        count
                      }
                    }
                  }
                }
                """.formatted(username);
        return rawQuery(query);
    }

    public String getProblemStats(final String username) {
        var query = """
                {
                  matchedUser(username: "%s") {
                    submitStats {
                      acSubmissionNum {
                        difficulty
                        count
                      }
                    }
                  }
                }
                """.formatted(username);
        return rawQuery(query);
    }

    public String getRecentSubmissions(final String username, int limit) {
        var query = """
                {
                  recentAcSubmissionList(username: "%s", limit: %d) {
                    title
                    titleSlug
                    timestamp
                    lang
                  }
                }
                """.formatted(username, limit);
        return rawQuery(query);
    }

    public String getContestHistory(final String username) {
        var query = """
                {
                  userContestRanking(username: "%s") {
                    rating
                    globalRanking
                    totalParticipants
                    topPercentage
                    attendedContestsCount
                  }
                }
                """.formatted(username);
        return rawQuery(query);
    }

    public List<String> getUserProfilesInParallel(final List<String> usernames) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = usernames.stream()
                    .map(username -> executor.submit(() -> getUserProfile(username)))
                    .toList();
            return futures.stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            return "Error fetching: " + e.getMessage();
                        }
                    })
                    .toList();
        }
    }

    public List<String> getContestHistoryInParallel(final List<String> usernames) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = usernames.stream()
                    .map(username -> executor.submit(() -> getContestHistory(username)))
                    .toList();
            return futures.stream()
                    .map(future -> {
                        try { return future.get(); }
                        catch (Exception e) { return "Error fetching: " + e.getMessage(); }
                    })
                    .toList();
        }
    }

    public List<String> getProblemStatsInParallel(final List<String> usernames) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = usernames.stream()
                    .map(username -> executor.submit(() -> getProblemStats(username)))
                    .toList();
            return futures.stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            return "Error fetching: " + e.getMessage();
                        }
                    })
                    .toList();
        }
    }

    private String rawQuery(final String graphqlQuery) {
        var body = "{\"query\": \"" + graphqlQuery.replace("\"", "\\\"").replace("\n", " ") + "\"}";
        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}