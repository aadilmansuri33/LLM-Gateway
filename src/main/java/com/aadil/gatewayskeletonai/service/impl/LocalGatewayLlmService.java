package com.aadil.gatewayskeletonai.service.impl;

import com.aadil.gatewayskeletonai.service.GatewayLlmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "llm.provider", havingValue = "local", matchIfMissing = true)
public class LocalGatewayLlmService implements GatewayLlmService {

    private static final Logger log = LoggerFactory.getLogger(LocalGatewayLlmService.class);

    @Override
    public String generate(String prompt) {
        log.debug("LocalGatewayLlmService.generate called (prompt length={})", prompt == null ? 0 : prompt.length());

        if (prompt == null || prompt.trim().isEmpty()) {
            log.warn("Empty prompt provided to LocalGatewayLlmService");
            return "No prompt provided.";
        }

        return "Local response: I received your prompt ==> " +
                prompt.trim() + " ==> and I'm running on machine!";
    }
}
