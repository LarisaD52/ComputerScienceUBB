package exceptions;

public class EmptyExecutionStackException extends RuntimeException {
    public EmptyExecutionStackException(String message) {
        super(message);
    }
}
