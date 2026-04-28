package com.example.leetcode_mcp.client;

import java.util.List;
import java.util.concurrent.Executors;

public interface CompetitiveProgrammingClient extends PlatformClient {

    String getProblemStats(String handle);
    String getContestHistory(String handle);

    default List<String> getProblemStatsInParallel(List<String> handles) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
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
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
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