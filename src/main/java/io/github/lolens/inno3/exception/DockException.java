package io.github.lolens.inno3.exception;

public class DockException extends Exception {

  public static final String DOCK_ALREADY_OCCUPIED = "Tried docking ship to an already occupied dock";

  public static final String DOCK_ALREADY_EMPTY = "Tried undocking ship from an empty dock";

  public DockException(String message) {
    super(message);
  }

  public DockException(String message, Throwable cause) {
    super(message, cause);
  }

  public DockException(Throwable cause) {
    super(cause);
  }

  public DockException() {
  }
}
