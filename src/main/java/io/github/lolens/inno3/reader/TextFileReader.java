package io.github.lolens.inno3.reader;

import io.github.lolens.inno3.exception.TextReaderException;

import java.util.List;

public interface TextFileReader {

    String readString() throws TextReaderException;

    List<String> lines() throws TextReaderException;

}
