package it.nicolasanti.manager;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.function.Function;

/**
 * Service class for managing order status.
 * Implements Function interface to process order status requests.
 */
@Service
public class OrderStatusService implements Function<OrderStatusService.Request, OrderStatusService.Response> {

    /**
     * Request record for order status inquiry.
     */
    @JsonClassDescription("Restituisce lo stato dell'ordine")
    public record Request(@JsonProperty(required = true, value = "order_code")
                              @JsonPropertyDescription("Il codice dell'ordine, e.g. XX-427") String orderCode) {
    }

    /**
     * Response record containing the order status.
     */
    public record Response(Status status) {
    }

    /**
     * Enum representing possible order statuses.
     */
    public enum Status {
        RICEIVED, SEND, REFIUSED, DELIVERED;

        /**
         * Returns a random status.
         * @return A randomly selected Status.
         */
        public static Status getRandom() {
            Status[] stati = values();
            return stati[new Random().nextInt(stati.length)];
        }
    }

    /**
     * Processes the order status request and returns a response.
     * @param request The order status request.
     * @return A response containing a random order status.
     */
    @Override
    public Response apply(Request request) {
        return new Response(Status.getRandom());
    }
}
