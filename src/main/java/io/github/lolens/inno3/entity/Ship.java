package io.github.lolens.inno3.entity;

import io.github.lolens.inno3.state.ShipState;
import io.github.lolens.inno3.state.WaitingState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Ship implements Callable<Ship.ExecutionResult> {

  private static final Logger logger = LoggerFactory.getLogger(Ship.class);

  private final int id;
  private final int capacity;
  private int currentLoad;

  private final int toLoad;
  private final int toUnload;

  private ShipState state;
  private Dock currentDock;

  public Ship(int shipId, int capacity, int currentLoad, int toLoad, int toUnload) {
    if (currentLoad > capacity) throw new IllegalArgumentException("Load can't be higher than capacity");
    this.id = shipId;
    this.capacity = capacity;
    this.currentLoad = currentLoad;
    this.toLoad = toLoad;
    this.toUnload = toUnload;
    this.state = new WaitingState();
  }

  public int getId() {
    return id;
  }

  public int getCapacity() {
    return capacity;
  }

  public int getCurrentLoad() {
    return currentLoad;
  }

  public int getToLoad() {
    return toLoad;
  }

  public int getToUnload() {
    return toUnload;
  }

  public void remove() {
    currentLoad--;
  }

  public void add() {
    currentLoad++;
  }

  public void dockTo(Dock dock) {
    this.currentDock = dock;
  }

  public Dock undock() {
    if (currentDock == null) throw new IllegalStateException("Ship tried to undock while not docked");
    Dock undockedFrom = currentDock;
    currentDock = null;
    return undockedFrom;
  }

  public Dock getCurrentDock() {
    return currentDock;
  }


  @Override
  public ExecutionResult call() throws Exception {
    int holdBeforeLoading = this.currentLoad;
    long startTime = System.nanoTime();

    try {
      while (state.hasNext()) {
        state = state.advance(this, Harbor.getInstance());
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt(); // Should returning thread have interrupted flag or not??
      logger.warn("Ship {} interrupted while in state {}", id, state.getName());
      throw e;
    } finally {
      if (currentDock != null) {
        Harbor.getInstance().undockShip(this);
      }
    }

    long endTime = System.nanoTime();
    long time = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
    return new ExecutionResult(
        id,                 // id
        time,               // time to complete
        holdBeforeLoading,  // initial cargo hold
        this.currentLoad,   // cargo hold after loading is done
        toLoad,             // count to load
        toUnload            // count to unload
    );
  }

  public record ExecutionResult(
      int id,
      long timeToEnd,

      int holdBeforeLoading,
      int holdAfterLoading,

      int toLoad,
      int toUnload
  ) {
  }
}
