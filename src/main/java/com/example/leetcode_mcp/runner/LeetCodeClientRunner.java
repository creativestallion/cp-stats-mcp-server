package com.example.leetcode_mcp.runner;

import com.example.leetcode_mcp.client.LeetCodeClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test-client")
public class LeetCodeClientRunner implements CommandLineRunner {

    private final LeetCodeClient client;

    public LeetCodeClientRunner(LeetCodeClient client) {
        this.client = client;
    }

    @Override
    public void run(String... args) {
        System.err.println("=== USER PROFILE ===");
        System.err.println(client.getProfile("prashuchaudhry"));

        System.err.println("=== PROBLEM STATS ===");
        System.err.println(client.getProblemStats("prashuchaudhry"));

        System.err.println("=== RECENT SUBMISSIONS ===");
        System.err.println(client.getRecentActivity("prashuchaudhry", 5));

        System.err.println("=== CONTEST HISTORY ===");
        System.err.println(client.getContestHistory("cosmicleo"));
    }
}
