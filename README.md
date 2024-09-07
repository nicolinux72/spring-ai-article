# RAG apps for legacy spring developers

_Introduction to RAG apps and review of the new Spring AI library_

The new Spring AI library is about to be released in version 1.0, what better opportunity to present the functioning of
RAG applications to us legacy Java developers?  
If you've been using Spring for a lifetime but don't know anything about LLM, this review will give you a comprehensible
explanation of how a chatbot-like application, summarizing or classifying documents, and querying private documents
works.
In this field, the langchain python library is the master, so it will be my concern to systematically compare it with
the implementation choices of Spring AI.
Italian prompts can, perhaps, be appreciated, to show a localized use of the famous ChatGPT.
As usual, the source code of the project is available on github and you will find a long annotated bibliography.

Read at [medium.com](https://medium.com/p/be6e2f75516e)

Leggi in italiano su [medium.com](https://medium.com/p/b5d5aea66f73)

## Quickstart

Prerequisites:

- Docker (Compose)
- JDK installation (17+)
- An OpenAI API key

For this example to work, you need to define a `secrets` profile with an OpenAI key defined, in
`src/main/resources/application-secrets.yaml`:

```yaml
spring:
  ai:
    openai:
      api-key: <your-api-key>
```

and then you can start the application by running:

```bash
./mvnw -Dspring-boot.run.profiles=secrets spring-boot:run
```

The first time you launch this command, it will download all the docker images needed for the application (Tempo,
PostgreSQL/pgvector, Grafana), next runs will be much faster.
You can find all the service definitions in `docker/docker-compose.yml`, feel free to change it if you need to.
Once docker compose dependencies are up and running, you should be able to call the `http://localhost:8080/ai` endpoint.

If you visit `http://localhost:3000` you will find a local Grafana instance that, under the `Explore > Tempo` tab will
let you explore API calls made to this application and all the tracing of internal calls (for example, calls to OpenAI).