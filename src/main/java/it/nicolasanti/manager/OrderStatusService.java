package it.nicolasanti.manager;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.function.Function;

@Service
public class OrderStatusService implements Function<OrderStatusService.Request, OrderStatusService.Response> {

    @JsonClassDescription("Restituisce lo stato dell'ordine")
    public record Request(@JsonProperty(required = true, value = "order_code")
                              @JsonPropertyDescription("Il codice dell'ordine, e.g. XX-427") String orderCode) {
    }

    //@JsonClassDescription("Restituisce lo stato dell'ordine")
    public record Response(Status status) {
    }

    public enum Status {
        RICEIVED, SEND, REFIUSED, DELIVERED;

        public static Status getRandom() {
            Status[] stati = values();
            return stati[new Random().nextInt(stati.length)];
        }
    }

    public Response apply(Request request) {
        return new Response(Status.getRandom());
    }
}
