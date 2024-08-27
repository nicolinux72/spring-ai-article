package it.nicolasanti.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
class Config {

    @Bean
    ChatMemory chatMemory() {
      return new InMemoryChatMemory();
    }

    // Here another method to instance and config a ChatClient
    // Not used in this project.
    @Value("classpath:/prompts/call-center-system.st")
    private Resource callCenterSystem;

    @Bean
    ChatClient callCenterClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem(callCenterSystem)
                .build();
    }

}
