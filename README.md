# LLM-Gateway
Designed for local development and easy extension to additional providers.
This repository contains a ready-to-import Maven project that demonstrates:
- Runtime provider selection via configuration
- A single REST endpoint: `POST /generate`
- Strongly-typed configuration and clear logging
- Java 25

Contents
- `pom.xml` — Maven build (Java 25)
- `src/main/java/...` — application code (package: `com.aadil.gatewayskeletonai`)
- `src/main/resources/application.yml` — default configuration

Prerequisites
- JDK 25 installed and configured (Project SDK in IntelliJ)
- Maven 3.8+ or use the included Maven
- (Optional) OpenAI API key if you want to make real OpenAI calls
    spring.ai.openai.api-key = "sk----" in yml or properties file

Quick start — import into IntelliJ
1. Open IntelliJ → File → Open → select the project's `pom.xml`.
2. Set Project SDK to JDK 25 (File → Project Structure → Project).
3. Wait for Maven import to finish.
4. Run the Spring Boot main class:
   `com.aadil.gatewayskeletonai.GatewaySkeletonAiApplication`

Running locally (safe default)
- The project defaults to the `local` provider and will not call external services.

mvn spring-boot:run -f pom.xml

Example (curl):
```bash
curl -s -X POST http://localhost:8080/generate \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Write a short aadil."}'
