package at.pranjic.application.mtcg.exceptions;

public class NoPackagesAvailableException extends RuntimeException {
  public NoPackagesAvailableException(String message) {
    super(message);
  }
}
