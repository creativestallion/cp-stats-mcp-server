package com.example.leetcode_mcp.runner;

import com.example.leetcode_mcp.client.GitHubClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("test-gh")
public class GitHubClientRunner implements CommandLineRunner {

    private final GitHubClient client;

    public GitHubClientRunner(GitHubClient client) {
        this.client = client;
    }

    @Override
    public void run(String... args) {
        System.err.println("=== GH PROFILE ===");
        System.err.println(client.getProfile("creativestallion"));

        System.err.println("=== GH REPOSITORIES ===");
        System.err.println(client.getRepositories("creativestallion"));

        System.err.println("=== GH RECENT ACTIVITY ===");
        System.err.println(client.getRecentActivity("creativestallion", 5));

        System.err.println("=== GH PARALLEL PROFILES ===");
        System.err.println(client.getProfilesInParallel(List.of("creativestallion", "torvalds")));

        System.err.println("=== INVALID USER ===");
        System.err.println(client.getProfile("invaliduser999xyz"));
    }
}