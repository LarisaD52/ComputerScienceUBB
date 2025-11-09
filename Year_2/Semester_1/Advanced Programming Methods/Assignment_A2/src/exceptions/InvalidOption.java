package exceptions;

public class InvalidOption extends RuntimeException {
  public InvalidOption(String message) {
    super(message);
  }
}
