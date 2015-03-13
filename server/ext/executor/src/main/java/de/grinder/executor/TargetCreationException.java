package de.grinder.executor;

public class TargetCreationException extends Exception {
  private static final long serialVersionUID = 1L;

  public TargetCreationException() {
    super();
  }

  public TargetCreationException(final String message) {
    super(message);
  }

  public TargetCreationException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public TargetCreationException(final Throwable cause) {
    super(cause);
  }

}
