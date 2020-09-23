package alushkja.blogpad.posts.control;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ConstaintViolationException extends WebApplicationException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ConstaintViolationException(String json) {
        super(Response.status(422)
                .entity(json)
                .build());
    }
}
