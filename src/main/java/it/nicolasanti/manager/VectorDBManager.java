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

@Service
public class VectorDBManager {

    @Autowired VectorStore vectorStore;

    void store() {
        List<Document> documents = List.of(new Document(getText("classpath:/data/fatti_curiosi.txt"), Map.of("meta1", "meta1")));
        vectorStore.add(documents);
    }

    void storeDocs(List<Document> docs) {
        vectorStore.write(docs);
    }

    public Collection<Document> retreive(String question) {
        var fb = new FilterExpressionBuilder();
        return vectorStore.similaritySearch((
                SearchRequest.query(question)
                        .withTopK(3)
                        .withFilterExpression( fb.eq("source", "article.docx").build())

        ));
    }

    List<Document> readWordDoc() {

        var wordDoc = new DefaultResourceLoader().getResource("classpath:/etl/article.docx");

        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(wordDoc);
        TokenTextSplitter splitter = new TokenTextSplitter(500, 400, 10, 1000, true);
        return  splitter.apply(tikaDocumentReader.read());
    }

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
