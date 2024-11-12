package at.pranjic.server.utils;

public class NoHttpStatusException extends RuntimeException {
    public NoHttpStatusException(String message) {
        super(message);
    }
}
