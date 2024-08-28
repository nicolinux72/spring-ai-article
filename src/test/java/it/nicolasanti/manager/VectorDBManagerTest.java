package it.nicolasanti.manager;

import it.nicolasanti.Application;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.List;

/**
 * This class is not a true unit test, but rather a simple client for the VectorDBManager service.
 * It demonstrates how to use VectorDBManager in a Spring Boot context.
 */
@SpringBootTest(classes = Application.class)
//@ExtendWith(MockitoExtension.class)
public class VectorDBManagerTest {

    private static final Logger logger = LoggerFactory.getLogger(VectorDBManagerTest.class);

    @Autowired private VectorDBManager vectorDBManager;

    @Test public void testStore() {
        vectorDBManager.store();
    }

    @Test public void testEtl() {
        List<Document> docs = vectorDBManager.readWordDoc();
        vectorDBManager.storeDocs(docs);
    }

    @Test public void testRetreive() {
        Collection<Document> documents = vectorDBManager.retreive("si intende per Memory?");
    }



}
