package com.example.leetcode_mcp;

import com.example.leetcode_mcp.tools.CodeforcesTools;
import com.example.leetcode_mcp.tools.LeetCodeTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LeetcodeMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeetcodeMcpApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider toolCallbackProvider(final LeetCodeTools lcTools, final CodeforcesTools cfTools) {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(lcTools, cfTools)
                .build();
    }
}