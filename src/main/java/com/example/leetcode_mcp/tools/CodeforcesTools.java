package com.example.leetcode_mcp.tools;

import com.example.leetcode_mcp.client.CodeforcesClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CodeforcesTools {

    private final CodeforcesClient client;

    public CodeforcesTools(CodeforcesClient client) {
        this.client = client;
    }

    @Tool(description = """
            Returns the Codeforces profile for one or more users including
            their rating, rank, name, and avatar URL.
            Pass a single handle or a list of handles to compare multiple users in parallel.
            Returns an error for invalid or non-existent handles.
            """)
    public String getCodeforcesProfile(final List<String> handles) {
        return client.getProfilesInParallel(handles)
                .stream()
                .map(result -> result.contains("\"status\":\"FAILED\"")
                        ? "User not found: " + result
                        : result)
                .reduce("", (a, b) -> a + "\n" + b);
    }

    @Tool(description = """
            Returns Codeforces problem solving stats for one or more users.
            Fetches recent submissions and returns accepted submission data.
            Pass a single handle or a list of handles to compare multiple users in parallel.
            """)
    public String getCodeforcesProblemStats(final List<String> handles) {
        return client.getProblemStatsInParallel(handles)
                .stream()
                .map(result -> result.contains("\"status\":\"FAILED\"")
                        ? "User not found: " + result
                        : result)
                .reduce("", (a, b) -> a + "\n" + b);
    }

    @Tool(description = """
            Returns recent Codeforces submissions for a single user.
            Includes problem name, verdict, programming language, and timestamp.
            Default limit is 10, maximum is 50.
            """)
    public String getCodeforcesRecentSubmissions(final String handle, int limit) {
        var result = client.getRecentSubmissions(handle, limit);
        if (result.contains("\"status\":\"FAILED\"")) {
            return "User not found: " + handle;
        }
        return result;
    }

    @Tool(description = """
            Returns full contest rating history for one or more Codeforces users
            including rating changes, contest names, rank, and timestamps.
            Pass a single handle or a list of handles to compare multiple users in parallel.
            """)
    public String getCodeforcesContestHistory(final List<String> handles) {
        return client.getContestHistoryInParallel(handles)
                .stream()
                .map(result -> result.contains("\"status\":\"FAILED\"")
                        ? "User not found: " + result
                        : result)
                .reduce("", (a, b) -> a + "\n" + b);
    }
}