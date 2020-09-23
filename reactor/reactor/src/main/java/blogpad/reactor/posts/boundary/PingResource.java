package blogpad.reactor.posts.boundary;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("ping")
public class PingResource {

    @Inject
    @ConfigProperty(name = "mp.message")
    String message;

    @GET
    public String ping() {
        return message;
    }

}
