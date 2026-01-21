package com.aadil.gatewayskeletonai.service.impl;

import com.aadil.gatewayskeletonai.service.GatewayLlmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "llm.provider", havingValue = "openai")
public class OpenAiGatewayLlmService implements GatewayLlmService {

    private static final Logger log = LoggerFactory.getLogger(OpenAiGatewayLlmService.class);

    @Value("${llm.openai.api-key:#{null}}")
    private String apiKey;

    @Value("${llm.openai.model:gpt-4o-mini}")
    private String model;

    @Value("${llm.openai.mock-template:Mocked OpenAI response: {prompt} (this is a simulation - no API key configured)}")
    private String mockTemplate;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String generate(String prompt) {
        log.info("Generating with OpenAI | prompt length: {}", prompt.length());

        String trimmedPrompt = prompt.trim();

        if (apiKey == null || apiKey.trim().isBlank()) {
            log.info("No OpenAI API key configured - falling back to mock mode");
            return mockTemplate.replace("{prompt}", trimmedPrompt);
        }

        log.info("Calling real OpenAI API | model: {}", model);

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(Map.of("role", "user", "content", trimmedPrompt))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey.trim());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    "https://api.openai.com/v1/chat/completions",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> body = responseEntity.getBody();
            if (body == null) {
                log.warn("Empty response body from OpenAI");
                return "Error: Empty response from OpenAI";
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> error = (Map<String, Object>) body.get("error");
            if (error != null) {
                String message = (String) error.get("message");
                log.warn("OpenAI API error: {}", message);
                return "OpenAI error: " + message;
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
            if (choices == null || choices.isEmpty()) {
                log.warn("No choices returned in OpenAI response");
                return "Error: No choices in OpenAI response";
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> messageMap = (Map<String, Object>) choices.get(0).get("message");
            String content = (String) messageMap.get("content");

            return content != null ? content.trim() : "Error: Empty content from OpenAI";

        } catch (Exception e) {
            log.error("Exception while calling OpenAI API", e);
            return "Failed to call OpenAI: " + e.getMessage();
        }
    }
}
