package com.example.leetcode_mcp.tools;

import com.example.leetcode_mcp.client.GitHubClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GitHubTools {

    private final GitHubClient client;

    public GitHubTools(GitHubClient client) {
        this.client = client;
    }

    @Tool(description = """
            Returns the GitHub profile for one or more users including
            their name, bio, public repo count, followers, following count,
            and account creation date.
            Pass a single username or a list of usernames to compare multiple users in parallel.
            Returns an error for invalid or non-existent usernames.
            """)
    public String getGitHubProfile(List<String> usernames) {
        return client.getProfilesInParallel(usernames)
                .stream()
                .map(result -> result.contains("\"error\"")
                        ? "User not found: " + result
                        : result)
                .reduce("", (a, b) -> a + "\n" + b);
    }

    @Tool(description = """
            Returns the top 10 public GitHub repositories by star count for one or more users.
            Each repo includes name, description, language, stars, forks, and last updated date.
            Pass a single username or a list of usernames to compare multiple users in parallel.
            Returns an error for invalid or non-existent usernames.
            """)
    public String getGitHubRepositories(List<String> usernames) {
        return client.getRepositoriesInParallel(usernames)
                .stream()
                .map(result -> result.contains("\"error\"")
                        ? "User not found: " + result
                        : result)
                .reduce("", (a, b) -> a + "\n" + b);
    }

    @Tool(description = """
            Returns the last 10 public GitHub events for a single user.
            Includes push events, pull requests, issues, comments, and stars.
            Use this to understand a developer's recent activity and contribution patterns.
            Returns an error for invalid or non-existent usernames.
            """)
    public String getGitHubRecentActivity(final String username, Integer capacity) {
        var result = client.getRecentActivity(username, capacity);
        if (result.contains("\"error\"")) {
            return "User not found: " + username;
        }
        return result;
    }
}