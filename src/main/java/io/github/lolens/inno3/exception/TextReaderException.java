package io.github.lolens.inno3.exception;

public class TextReaderException extends Exception {

  public static final String FILE_NOT_FOUND = "Specified file doesn't exist";
  public static final String IO_EXCEPTION = "Encountered IOException while reading a file";

  public TextReaderException(String message) {
    super(message);
  }

  public TextReaderException() {
    super();
  }

  public TextReaderException(String message, Throwable cause) {
    super(message, cause);
  }

  public TextReaderException(Throwable cause) {
    super(cause);
  }

}
