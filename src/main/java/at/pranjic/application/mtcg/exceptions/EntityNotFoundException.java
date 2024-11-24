package at.pranjic.application.mtcg.exceptions;

public class EntityNotFoundException extends RuntimeException {
  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(String type, int id) {
    super("%s: %s not found");
  }
}