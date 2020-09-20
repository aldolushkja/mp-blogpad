package airhacks.service.ping.boundary;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 * @author airhacks.com
 */
public class PingResourceIT {

    private ReactorResourceClient client;

    @BeforeEach
    public void init() {
        URI uri = URI.create("http://localhost:8080/reactor/resources/");
        this.client = RestClientBuilder.
                newBuilder().
                baseUri(uri).
                build(ReactorResourceClient.class);

    }

    @Test
    public void ping() {
        Response response = this.client.ping();
        int status = response.getStatus();
        assertEquals(200, status);
        String message = response.readEntity(String.class);
        assertNotNull(message);
        System.out.println(message);
        
    }
}