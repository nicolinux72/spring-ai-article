package it.nicolasanti.manager;

import it.nicolasanti.utils.ClearThreadUnsafeLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for managing AI agent interactions.
 * It configures and uses a ChatClient to process user messages.
 */
@Service
public class AgentManager {

    ChatClient chatClient;

    /**
     * Constructor for AgentManager.
     * Initializes the ChatClient with custom configurations.
     *
     * @param toolSystem Resource containing the system prompt for the tool
     * @param builder ChatClient.Builder to construct the ChatClient
     */
    public AgentManager(@Value("classpath:/prompts/tool-system.st") Resource toolSystem,
                        ChatClient.Builder builder) {

        chatClient =  builder
                .defaultSystem(toolSystem)
                .defaultAdvisors(new ClearThreadUnsafeLoggerAdvisor())
                .defaultOptions(OpenAiChatOptions.builder()
                        .withFunction("orderStatusService")
                        .build())
                .build();
    }

    /**
     * Processes a user message using the configured ChatClient.
     *
     * @param message The user's input message
     * @return The response content from the AI
     */
    public String tools(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

}
