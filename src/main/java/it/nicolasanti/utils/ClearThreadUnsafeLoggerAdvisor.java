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

public class ClearThreadUnsafeLoggerAdvisor implements RequestResponseAdvisor {

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

    public Flux<ChatResponse> adviseResponse(Flux<ChatResponse> fluxChatResponse, Map<String, Object> context) {
        return (new MessageAggregator()).aggregate(fluxChatResponse, System.out::println);
    }

    public ChatResponse adviseResponse(ChatResponse response, Map<String, Object> context) {
        System.out.println("response:");

        for (Generation result : response.getResults()) {
            System.out.printf("   ASSISTANT: %s%n", result.getOutput().getContent());
        }
        return response;
    }

    public String toString() {
        return ClearThreadUnsafeLoggerAdvisor.class.getSimpleName();
    }
}
