package com.aadil.gatewayskeletonai.controller;

import com.aadil.gatewayskeletonai.dto.GenerateRequest;
import com.aadil.gatewayskeletonai.dto.GenerateResponse;
import com.aadil.gatewayskeletonai.service.GatewayLlmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class GatewayController {
    private static final Logger log = LoggerFactory.getLogger(GatewayController.class);

    private final GatewayLlmService gatewayLlmService;

    public GatewayController(GatewayLlmService gatewayLlmService) {
        this.gatewayLlmService = gatewayLlmService;
    }

    @PostMapping(path = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public GenerateResponse generate(@RequestBody GenerateRequest request) {
        String prompt = request == null ? null : request.prompt();
        log.info("POST /generate received (prompt length={})", prompt == null ? 0 : prompt.length());

        if (prompt == null || prompt.trim().isEmpty()) {
            log.warn("Empty prompt in request");
            return new GenerateResponse("Prompt cannot be empty.");
        }

        try {
            String result = gatewayLlmService.generate(prompt);
            log.debug("Generated response ({} chars)", result == null ? 0 : result.length());
            return new GenerateResponse(result);
        } catch (Exception ex) {
            log.error("Failed to generate response: {}", ex.getMessage(), ex);
            return new GenerateResponse("Failed to generate response: " + ex.getMessage());
        }
    }
}
