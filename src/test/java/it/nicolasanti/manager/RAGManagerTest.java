package it.nicolasanti.manager;

import it.nicolasanti.Application;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
//@ExtendWith(MockitoExtension.class)
public class RAGManagerTest {

    private static final Logger logger = LoggerFactory.getLogger(RAGManagerTest.class);
    @Autowired private RAGManager ragManager;

    @Test
    public void testChatResponse() {
        //assertNotNull(response);
        //ragManager.ragResponse("Cosa si intente per Memory");
        ragManager.ragResponse("C'è un'analogia tra legacy db app e LLL app?");

    }



}
