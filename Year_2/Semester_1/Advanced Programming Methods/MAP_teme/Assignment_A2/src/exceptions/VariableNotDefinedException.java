package exceptions;

public class VariableNotDefinedException extends RuntimeException {
    public VariableNotDefinedException(String message) {
        super(message);
    }
}
