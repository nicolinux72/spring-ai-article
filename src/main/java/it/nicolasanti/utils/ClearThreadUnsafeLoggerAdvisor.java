package it.nicolasanti.utils;

import org.springframework.ai.chat.client.AdvisedRequest;
import org.springframework.ai.chat.client.RequestResponseAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.model.MessageAggregator;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * This class implements the RequestResponseAdvisor interface to provide logging functionality
 * for AI chat requests and responses. It is not thread-safe and should not be used 
 * in multi-threaded environments.
 */
public class ClearThreadUnsafeLoggerAdvisor implements RequestResponseAdvisor {

    /**
     * Advises the request by logging its contents.
     * @param request The advised request
     * @param context The context map
     * @return The original request
     */
    public AdvisedRequest adviseRequest(AdvisedRequest request, Map<String, Object> context) {
        System.out.println("request:");
        System.out.printf("   SYSTEM: %s%n", request.systemText());

        for (Message message : request.messages()) {
                System.out.printf("   %s: %s%n", message.getMessageType(), message.getContent());
        }

        if (!CollectionUtils.isEmpty(request.userParams())) {
            System.out.printf("   USER: %s%n", new PromptTemplate(request.userText(), request.userParams()).render());
        } else {
            System.out.printf("   USER: %s%n", request.userText());
        }
        return request;
    }

    /**
     * Advises the response by aggregating and logging its contents.
     * @param fluxChatResponse The flux of chat responses
     * @param context The context map
     * @return The aggregated and logged flux of chat responses
     */
    public Flux<ChatResponse> adviseResponse(Flux<ChatResponse> fluxChatResponse, Map<String, Object> context) {
        return (new MessageAggregator()).aggregate(fluxChatResponse, System.out::println);
    }

    /**
     * Advises a single chat response by logging its contents.
     * @param response The chat response
     * @param context The context map
     * @return The original chat response
     */
    public ChatResponse adviseResponse(ChatResponse response, Map<String, Object> context) {
        System.out.println("response:");

        for (Generation result : response.getResults()) {
            System.out.printf("   ASSISTANT: %s%n", result.getOutput().getContent());
        }
        return response;
    }

    /**
     * Returns the simple name of this class.
     * @return The simple name of the class
     */
    public String toString() {
        return ClearThreadUnsafeLoggerAdvisor.class.getSimpleName();
    }
}
