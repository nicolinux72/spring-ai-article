package it.nicolasanti.manager;

import it.nicolasanti.Application;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This class is not a true unit test, but rather a simple client for the AgentManager service.
 * It demonstrates how to use AgentManager in a Spring Boot context.
 */
@SpringBootTest(classes = Application.class)
//@ExtendWith(MockitoExtension.class)
public class AgentManagerTest {

    private static final Logger logger = LoggerFactory.getLogger(AgentManagerTest.class);
    @Autowired private AgentManager agentManager;

    @Test
    public void testTool() {
        //assertNotNull(response);
        agentManager.tools("In quale stato è l'ordine con codice AB-621?");

    }



}
