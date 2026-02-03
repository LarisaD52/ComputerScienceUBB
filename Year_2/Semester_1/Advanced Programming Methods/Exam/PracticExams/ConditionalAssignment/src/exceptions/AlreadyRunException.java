package exceptions;

public class AlreadyRunException extends RuntimeException {
  public AlreadyRunException(String message) {
    super(message);
  }
}
