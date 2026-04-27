# LeetCode MCP Server

A [Model Context Protocol (MCP)](https://modelcontextprotocol.io) server built with **Java 21** and **Spring Boot**, that exposes your LeetCode profile data as tools for LLMs like Claude Desktop and Cursor.

Ask Claude things like:
- _"How many medium problems this user solved?"_
- _"What's the current contest rating?"_
- _"Show me the last 10 submissions"_
- _"Compare problem stats for userA, userB, userC.."_

---

## Demo

> _Add your GIF here — record a short Claude Desktop session querying your profile_

---

## Architecture

```
Claude Desktop
     │
     │  stdio (JSON-RPC)
     ▼
Spring Boot MCP Server  ──►  LeetCode GraphQL API
(your tools run here)         https://leetcode.com/graphql
```

- Claude Desktop launches the JAR as a subprocess
- Communication happens over **stdio** using the MCP protocol
- The server calls LeetCode's **unauthenticated public GraphQL API**
- Multi-user queries run in **parallel using Java 21 virtual threads**

---

## Tools Exposed

| Tool | Description | Multi-user |
|------|-------------|------------|
| `getUserProfile` | Global ranking, reputation, star rating, solve counts | ✅ parallel |
| `getProblemStats` | Easy / Medium / Hard breakdown | ✅ parallel |
| `getRecentSubmissions` | Last N accepted submissions with language and timestamp | ❌ single user |
| `getContestHistory` | Contest rating, global rank, top %, contests attended | ✅ parallel |

---

## Tech Stack

- Java 21 (virtual threads for parallel API calls)
- Spring Boot 3.5.x
- Spring AI 1.0.1 — MCP Server starter
- Spring WebFlux — WebClient for GraphQL calls
- Docker

---

## Quick Start

### Prerequisites
- Java 21+
- Maven 3.9+
- [Claude Desktop](https://claude.ai/download)

### 1. Clone and build

```bash
git clone https://github.com/creativestallion/leetcode-mcp-server.git
cd leetcode-mcp-server
mvn clean package -DskipTests
```

### 2. Configure Claude Desktop

Open your Claude Desktop config file:

```bash
# Mac
open ~/Library/Application\ Support/Claude/
```

Edit `claude_desktop_config.json` and add:

```json
{
  "mcpServers": {
    "leetcode": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/leetcode-mcp-server/target/leetcode-mcp-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

### 3. Restart Claude Desktop

Fully quit (Cmd+Q) and reopen. You should see the hammer 🔨 icon in the chat input — that means the MCP server is connected.

### 4. Try it

```
What's the LeetCode profile for prashuchaudhry?
Compare problem stats for prashuchaudhry and neal_wu
Show me the last 5 submissions for prashuchaudhry
What's the contest rating for prashuchaudhry?
```

---

## Run with Docker

```bash
docker build -t leetcode-mcp-server .
```

To use the Docker image with Claude Desktop, update your config:

```json
{
  "mcpServers": {
    "leetcode": {
      "command": "docker",
      "args": [
        "run",
        "--rm",
        "-i",
        "leetcode-mcp-server"
      ]
    }
  }
}
```

---

## Development

### Run locally

```bash
mvn spring-boot:run
```

### Test the GraphQL client directly

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test-client
```

### Project structure

```
src/main/java/com/example/leetcode_mcp/
├── LeetcodeMcpApplication.java       # App entry point, tool registration
├── client/
│   ├── LeetCodeClient.java           # WebClient + GraphQL queries
│   └── LeetCodeClientRunner.java     # Dev test runner (test-client profile)
└── tools/
    └── LeetCodeTools.java            # @Tool definitions exposed via MCP
```

---

## Key Design Decisions

**Why virtual threads?**
Multi-user queries (e.g. comparing 3 candidates) fire all API calls in parallel using `Executors.newVirtualThreadPerTaskExecutor()`. This cuts latency proportionally to the number of users with zero thread pool tuning.

**Why stdio transport?**
Claude Desktop communicates with MCP servers over stdin/stdout. No HTTP server needed, no ports, no auth. The JAR is launched as a subprocess and exits when the session ends.

**Why no database or cache?**
LeetCode's public API responds in ~200ms. The server is stateless by design — no persistence layer means nothing to operate, nothing to secure, nothing to break.

**Why no authentication?**
All data queried is publicly visible on LeetCode profiles. No login, no API key, no OAuth required.

---

## About

Built as a portfolio project to demonstrate:
- Spring AI MCP server implementation
- Java 21 virtual threads for I/O parallelism
- GraphQL API integration with WebClient
- Clean tool design for LLM consumption

---

## License

MIT