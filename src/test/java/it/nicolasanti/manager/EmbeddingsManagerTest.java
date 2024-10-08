package it.nicolasanti.manager;

import it.nicolasanti.Application;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * This class is not a true unit test, but rather a simple client for the EmbeddingsManager service.
 * It demonstrates how to use EmbeddingsManager in a Spring Boot context.
 */
@SpringBootTest(classes = Application.class)
//@ExtendWith(MockitoExtension.class)
public class EmbeddingsManagerTest {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddingsManagerTest.class);
    @Autowired private EmbeddingsManager embeddingsManager;

    @Test
    public void testEmbeddings() {
        EmbeddingResponse encoded = embeddingsManager.encode(List.of("Proviamo con qualcosa"));

    }


    

}
