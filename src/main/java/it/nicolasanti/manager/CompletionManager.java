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

@Service
public class CompletionManager {

    private final OpenAiChatModel openAiChatModel;
    ChatClient chatClient;

    public CompletionManager(@Value("classpath:/prompts/flowers-system.st") Resource flowersSystem,
                             ChatClient.Builder builder, ChatMemory chatMemory, OpenAiChatModel openAiChatModel) {
        this.openAiChatModel = openAiChatModel;
        chatClient =  builder
                .defaultSystem(flowersSystem)
                .defaultAdvisors(List.of(new MessageChatMemoryAdvisor(chatMemory) ))
                .build();
    }

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
