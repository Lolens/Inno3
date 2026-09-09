package io.github.lolens.inno3.reader.impl;

import io.github.lolens.inno3.exception.TextReaderException;
import io.github.lolens.inno3.reader.TextFileReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TextFileReaderImpl implements TextFileReader {

  private Path path;

  public TextFileReaderImpl() {

  }

  public TextFileReaderImpl(Path path) {
    this.path = path;
  }

  public void changeFilePath(Path path) {
    this.path = path;
  }

  public String readString() throws TextReaderException {
    try {
      return Files.readString(path, StandardCharsets.UTF_8);
    } catch (FileNotFoundException e) {
      throw new TextReaderException(TextReaderException.FILE_NOT_FOUND, e);
    } catch (IOException e) {
      throw new RuntimeException(TextReaderException.IO_EXCEPTION, e);
    }
  }

  public List<String> lines() throws TextReaderException {
    try {
      return Files.readAllLines(path, StandardCharsets.UTF_8);
    } catch (FileNotFoundException e) {
      throw new TextReaderException(TextReaderException.FILE_NOT_FOUND, e);
    } catch (IOException ioe) {
      throw new TextReaderException(TextReaderException.IO_EXCEPTION, ioe);
    }
  }

}
