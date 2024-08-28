package it.nicolasanti.manager;

import it.nicolasanti.Application;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This class is not a true unit test, but rather a simple client for the CompletionManager service.
 * It demonstrates how to use CompletionManager in a Spring Boot context.
 */
@SpringBootTest(classes = Application.class)
//@ExtendWith(MockitoExtension.class)
public class CompletionManagerTest {

    private static final Logger logger = LoggerFactory.getLogger(CompletionManagerTest.class);
    @Autowired private CompletionManager completionManager;

    @Test
    public void testChatResponse() {
        //assertNotNull(response);
        completionManager.chatResponse("Quali sono le prime tre nazioni esportatrici di tulipani?");
        completionManager.chatResponse("E la quarta?");

    }

    @Test
    public void testEntityResponse() {
        //assertNotNull(response);
        CompletionManager.Works entity = completionManager.entity("Riporta la lista delle più belle poesie di Wisława Szymborska in formato json. ");
    }


}
