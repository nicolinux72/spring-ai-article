package it.nicolasanti.manager;

import it.nicolasanti.utils.ClearThreadUnsafeLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Retrieval-Augmented Generation (RAG) operations.
 * This class sets up and uses a ChatClient with various advisors for enhanced chat functionality.
 */
@Service
public class RAGManager {

    ChatClient chatClient;

    /**
     * Constructor that initializes the ChatClient with specific configurations and advisors.
     *
     * @param flowersSystem   Resource containing the system prompt for the chat
     * @param builder         ChatClient.Builder for constructing the ChatClient
     * @param chatMemory      ChatMemory for maintaining conversation context
     * @param openAiChatModel OpenAiChatModel for the chat model
     * @param vectorStore     VectorStore for semantic search capabilities
     */
    public RAGManager(@Value("classpath:/prompts/rag-system.st") Resource flowersSystem,
                      ChatClient.Builder builder, ChatMemory chatMemory, OpenAiChatModel openAiChatModel, VectorStore vectorStore) {
        chatClient = builder
                    .defaultSystem(flowersSystem)
                    .defaultAdvisors(List.of(new MessageChatMemoryAdvisor(chatMemory),
                                             new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()
                                                                           .withTopK(3)),
                                             new ClearThreadUnsafeLoggerAdvisor()))
                    .build();
    }

    /**
     * Generates a response using the RAG approach.
     *
     * @param message The user's input message
     * @return The generated response from the chat model
     */
    public String ragResponse(String message) {
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "source == 'article.docx'"))
                .call()
                .content();
    }
}
