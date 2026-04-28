package com.example.leetcode_mcp.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class LeetCodeClient implements PlatformClient {

    private final WebClient webClient;

    public LeetCodeClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://leetcode.com/graphql")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Referer", "https://leetcode.com")
                .build();
    }

    @Override
    public String getProfile(final String username) {
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

    @Override
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

    @Override
    public String getRecentSubmissions(final String username, int limit) {
        if (limit < 1) limit = 10;
        if (limit > 20) limit = 20;
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

    @Override
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

    private String rawQuery(final String graphqlQuery) {
        var body = "{\"query\": \"" + graphqlQuery.replace("\"", "\\\"").replace("\n", " ") + "\"}";
        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}