package com.example.leetcode_mcp.client;

import java.util.List;
import java.util.concurrent.Executors;

public interface PlatformClient {

    String getProfile(String handle);
    String getRecentActivity(String handle, int limit);

    default List<String> getProfilesInParallel(List<String> handles) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = handles.stream()
                    .map(handle -> executor.submit(() -> getProfile(handle)))
                    .toList();
            return futures.stream()
                    .map(future -> {
                        try { return future.get(); }
                        catch (Exception e) { return "Error fetching: " + e.getMessage(); }
                    })
                    .toList();
        }
    }
}