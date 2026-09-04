package common;

import java.io.Serializable;
import java.util.Collection;

public class Response implements Serializable {
    private static final long serialVersionUID = 2L;

    private final String message;
    private final Collection<?> collection;

    public Response(String message) {
        this(message, null);
    }

    public Response(String message, Collection<?> collection) {
        this.message = message;
        this.collection = collection;
    }

    public String getMessage() { return message; }
    public Collection<?> getCollection() { return collection; }
}