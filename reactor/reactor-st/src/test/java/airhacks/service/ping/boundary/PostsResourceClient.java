package airhacks.service.ping.boundary;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("posts")
public interface PostsResourceClient {

    @GET
    Response ping();
    
}