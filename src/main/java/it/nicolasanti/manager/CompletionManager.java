package it.nicolasanti.manager;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.nicolasanti.utils.ClearThreadUnsafeLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.ResponseFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Manages chat completions and response generation using an AI chat model.
 * This class provides functionality for generating chat responses and structuring information about Polish poetry.
 */
@Service
public class CompletionManager {

    ChatClient chatClient;

    /**
     * Constructs a CompletionManager with the necessary dependencies.
     * Initializes the chatClient with default system prompt and memory advisor.
     *
     * @param flowersSystem Resource containing the system prompt
     * @param builder ChatClient.Builder for constructing the chatClient
     * @param chatMemory ChatMemory for maintaining conversation history
     */
    public CompletionManager(@Value("classpath:/prompts/flowers-system.st") Resource flowersSystem,
                             ChatClient.Builder builder, ChatMemory chatMemory) {

        chatClient =  builder
                .defaultSystem(flowersSystem)
                .defaultAdvisors(List.of(new MessageChatMemoryAdvisor(chatMemory) ))
                .build();
    }

    /**
     * Generates a chat response based on the given user message.
     *
     * @param message The input message from the user
     * @return The generated response content as a String
     */
    public String chatResponse(String message) {
        return chatClient.prompt()
                //.advisors(new SimpleLoggerAdvisor())
                .advisors(new ClearThreadUnsafeLoggerAdvisor())
                .user(message)
                .call()
                .content();
    }

    record Works(@JsonProperty(required = true, value = "works") Work[] works,
                 @JsonProperty(required = true, value = "poet_name") String poet) {

            record Work(@JsonProperty(required = true, value = "title") String title,
                        @JsonProperty(required = true, value = "year") Integer year) {
            }
    }


    /**
     * Generates a structured response about Polish poetry works based on the given message.
     * Uses a custom output converter to format the response as a Works object.
     *
     * @param message The input message from the user
     * @return A Works object containing information about Polish poetry works and the poet
     */
    public Works entity(String message) {

        var outputConverter = new BeanOutputConverter<>(CompletionManager.Works.class);

        ChatResponse response =chatClient.prompt()
                .advisors(new ClearThreadUnsafeLoggerAdvisor())
                .system("Sei un fine letterato, amante della poesia polacca e desideroso di rendela noto in tutto il mondo.")
                .options(OpenAiChatOptions.builder()
                        .withModel(OpenAiApi.ChatModel.GPT_4_O_MINI)
                        .withResponseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, outputConverter.getJsonSchema()))
                        .build())
                .user(message)
                .call()
                .chatResponse();

        String content = response.getResult().getOutput().getContent();

        return outputConverter.convert(content);

    }
}
