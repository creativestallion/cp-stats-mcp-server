package com.example.leetcode_mcp.runner;

import com.example.leetcode_mcp.client.CodeforcesClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("test-cf")
public class CodeforcesClientRunner implements CommandLineRunner {

    private final CodeforcesClient client;

    public CodeforcesClientRunner(CodeforcesClient client) {
        this.client = client;
    }

    @Override
    public void run(String... args) {
        System.err.println("=== CF PROFILE ===");
        System.err.println(client.getProfile("tourist"));

        System.err.println("=== CF PROBLEM STATS ===");
        System.err.println(client.getProblemStats("tourist"));

        System.err.println("=== CF RECENT SUBMISSIONS ===");
        System.err.println(client.getRecentSubmissions("tourist", 5));

        System.err.println("=== CF CONTEST HISTORY ===");
        System.err.println(client.getContestHistory("tourist"));

        System.err.println("=== CF PARALLEL PROFILES ===");
        System.err.println(client.getProfilesInParallel(List.of("tourist", "invalidhandle999xyz")));
    }
}