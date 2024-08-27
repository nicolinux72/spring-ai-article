package it.nicolasanti.manager;

import it.nicolasanti.utils.ClearThreadUnsafeLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class AgentManager {

    ChatClient chatClient;

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

    public String tools(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

}
