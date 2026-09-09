package io.github.lolens.inno3;

import io.github.lolens.inno3.entity.Ship;
import io.github.lolens.inno3.exception.TextReaderException;
import io.github.lolens.inno3.parser.ShipParser;
import io.github.lolens.inno3.parser.impl.ShipParserImpl;
import io.github.lolens.inno3.reader.TextFileReader;
import io.github.lolens.inno3.reader.impl.TextFileReaderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.*;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private static final Path FILE_PATH;

  static {
    try {
      FILE_PATH = getFilePathFromResources();
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize FILE_PATH", e);
    }
  }

  private static Path getFilePathFromResources() throws FileNotFoundException, URISyntaxException {
    URL fileURL = Main.class.getClassLoader().getResource("input.txt");
    if (fileURL == null) throw new FileNotFoundException("Specified file is null.");
    return Path.of(fileURL.toURI());
  }

  public static void main(String[] args) {
    TextFileReader reader = new TextFileReaderImpl(FILE_PATH);
    ShipParser parser = new ShipParserImpl();

    List<String> lines;
    try {
      lines = reader.lines();
    } catch (TextReaderException e) {
      logger.error("Reader failed to read file.", e);
      throw new RuntimeException(e);
    }

    List<Ship> ships = parser.parse(lines);

    try (ExecutorService executorService = Executors.newFixedThreadPool(ships.size())) {

      List<Future<Ship.ExecutionResult>> futures = ships.stream()
          .map(executorService::submit)
          .toList();

      executorService.shutdown();

      if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
        logger.info("Executor service shut down successfully");
      } else {
        logger.warn("Executor service terminated without finishing all tasks");
      }

      for (Future<Ship.ExecutionResult> future : futures) {
        try {
          Ship.ExecutionResult result = future.get();
          logger.info(
              """
              
              ===== SHIP =====
              Id: {}
              To load: {}
              To unload: {}
              
              Held before loading: {}
              Held after loading: {}
              
              Time to end: {}
              =================
              """,
              result.id(), result.toLoad(), result.toUnload(), result.holdBeforeLoading(), result.holdAfterLoading(), result.timeToEnd()
          );
        } catch (ExecutionException e) {
          logger.error("Failed to retrieve execution result", e.getCause());
        }
      }

    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }


  }

}
