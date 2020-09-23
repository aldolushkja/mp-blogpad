package alushkja.blogpad.posts.control;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class StorageException extends WebApplicationException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public StorageException(String message, Throwable cause) {
        super(Response.status(400)
                .header("message", message)
                .header("cause", cause.getMessage())
                .build());
    }

    public StorageException(String message) {
        super(Response.status(400)
                .header("message", message)
                .build());
    }


}
