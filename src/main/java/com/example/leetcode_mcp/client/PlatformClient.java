package com.example.leetcode_mcp.client;

import java.util.List;

public interface PlatformClient {
    String getProfile(final String handle);
    String getProblemStats(final String handle);
    String getRecentSubmissions(final String handle, int limit);
    String getContestHistory(final String handle);

    default List<String> getProfilesInParallel(List<String> handles) {
        try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
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

    default List<String> getProblemStatsInParallel(List<String> handles) {
        try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = handles.stream()
                    .map(handle -> executor.submit(() -> getProblemStats(handle)))
                    .toList();
            return futures.stream()
                    .map(future -> {
                        try { return future.get(); }
                        catch (Exception e) { return "Error fetching: " + e.getMessage(); }
                    })
                    .toList();
        }
    }

    default List<String> getContestHistoryInParallel(List<String> handles) {
        try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = handles.stream()
                    .map(handle -> executor.submit(() -> getContestHistory(handle)))
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