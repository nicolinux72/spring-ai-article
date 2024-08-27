package it.nicolasanti.manager;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing vector database operations.
 */
@Service
public class VectorDBManager {

    @Autowired VectorStore vectorStore;

    /**
     * Stores a predefined document in the vector store.
     */
    void store() {
        List<Document> documents = List.of(new Document(getText("classpath:/data/fatti_curiosi.txt"), Map.of("meta1", "meta1")));
        vectorStore.add(documents);
    }

    /**
     * Stores a list of documents in the vector store.
     * @param docs List of documents to store
     */
    void storeDocs(List<Document> docs) {
        vectorStore.write(docs);
    }

    /**
     * Retrieves documents similar to the given question from the vector store.
     * @param question The query string
     * @return Collection of similar documents
     */
    public Collection<Document> retreive(String question) {
        var fb = new FilterExpressionBuilder();
        return vectorStore.similaritySearch((
                SearchRequest.query(question)
                        .withTopK(3)
                        .withFilterExpression( fb.eq("source", "article.docx").build())

        ));
    }

    /**
     * Reads a Word document, splits it into chunks, and returns a list of documents.
     * @return List of Document objects created from the Word file
     */
    List<Document> readWordDoc() {

        var wordDoc = new DefaultResourceLoader().getResource("classpath:/etl/article.docx");

        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(wordDoc);
        TokenTextSplitter splitter = new TokenTextSplitter(500, 400, 10, 1000, true);
        return  splitter.apply(tikaDocumentReader.read());
    }

    /**
     * Reads the content of a file from the given URI.
     * @param uri The URI of the file to read
     * @return The content of the file as a String
     * @throws RuntimeException if an IOException occurs while reading the file
     */
    public static String getText(String uri) {
        var resource = new DefaultResourceLoader().getResource(uri);
        try {
            return resource.getContentAsString(StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
