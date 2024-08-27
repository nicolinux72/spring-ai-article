package it.nicolasanti.manager;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class EmbeddingsManager {

    @Autowired
    EmbeddingModel embeddingModel;

    public EmbeddingResponse encode(List<String> texts) {

        if (isEmpty(texts)) {
            texts = List.of("Hello World", "World is big and salvation is near");
        }

        return embeddingModel.call(
                new EmbeddingRequest(texts,
                        OpenAiEmbeddingOptions.builder()
                                //.withModel("text-embedding-ada-002")
                                .build()));

    }
}
