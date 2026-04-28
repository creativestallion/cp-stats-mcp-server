package com.example.leetcode_mcp.tools;

import com.example.leetcode_mcp.client.LeetCodeClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LeetCodeTools {

    private final LeetCodeClient client;

    public LeetCodeTools(LeetCodeClient client) {
        this.client = client;
    }

    @Tool(description = """
            Returns the LeetCode profile for one or more users including global ranking,
            reputation, star rating, and total accepted submissions by difficulty (Easy, Medium, Hard).
            Pass a single username or a list of usernames to compare multiple users in parallel.
            Returns an error message for any invalid or non-existent username.
            """)
    public String getUserProfile(final List<String> usernames) {
        return client.getProfilesInParallel(usernames)
                .stream()
                .map(result -> result.contains("\"matchedUser\":null")
                        ? "User not found: " + result
                        : result)
                .reduce("", (a, b) -> a + "\n" + b);
    }

    @Tool(description = """
            Returns problems solved broken down by difficulty (Easy, Medium, Hard)
            for one or more LeetCode users, fetched in parallel.
            Pass a single username or a list of usernames to compare multiple users.
            Returns an error message for any invalid or non-existent username.
            """)
    public String getProblemStats(final List<String> usernames) {
        return client.getProblemStatsInParallel(usernames)
                .stream()
                .map(result -> result.contains("\"matchedUser\":null")
                        ? "User not found: " + result
                        : result)
                .reduce("", (a, b) -> a + "\n" + b);
    }

    @Tool(description = """
            Returns the last N accepted LeetCode submissions for a single user.
            Each submission includes problem title, slug, programming language, and timestamp.
            Default limit is 10, maximum is 20.
            Returns an error message if the username is invalid.
            """)
    public String getRecentSubmissions(final String username, int limit) {
        if (limit < 1) limit = 10;
        if (limit > 20) limit = 20;
        var result = client.getRecentActivity(username, limit);
        if (result.contains("\"recentAcSubmissionList\":null")) {
            return "User not found: " + username;
        }
        return result;
    }

    @Tool(description = """
        Returns contest history for one or more LeetCode users including contest rating,
        global ranking, total participants, top percentage, and contests attended.
        Pass a single username or a list of usernames to compare multiple users in parallel.
        Returns an error message if the username is invalid or user has no contest history.
        """)
    public String getContestHistory(final List<String> usernames) {
        return client.getContestHistoryInParallel(usernames)
                .stream()
                .map(result -> result.contains("\"userContestRanking\":null")
                        ? "User not found or no contest history: " + result
                        : result)
                .reduce("", (a, b) -> a + "\n" + b);
    }
}