package io.github.lolens.inno3.entity;

import io.github.lolens.inno3.state.ShipState;
import io.github.lolens.inno3.state.WaitingState;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Ship implements Callable<Ship.ExecutionResult> {

    private final int id;
    private final int capacity;
    private int currentLoad;

    private final int toLoad;
    private final int toUnload;

    private ShipState state;
    private Dock currentDock;

  public Ship(int shipId, int capacity, int currentLoad, int toLoad, int dockToUnload) {
    if (currentLoad > capacity) throw new IllegalArgumentException("Load can't be higher than capacity");
    this.id = shipId;
    this.currentLoad = currentLoad;
    this.capacity = capacity;
    this.toLoad = toLoad;
    this.toUnload = dockToUnload;
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

  public int remove() {
    return currentLoad--;
  }

  public int add() {
    return currentLoad++;
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
    long startTime = System.nanoTime();

    while (!state.isLast()) {
      state = state.advance(this, Harbor.getInstance());
    }

    long endTime = System.nanoTime();

    long time = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
    return new ExecutionResult(id, time);
  }

  public record ExecutionResult(int id, long timeToEnd) {}
}
