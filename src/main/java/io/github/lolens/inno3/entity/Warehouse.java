package io.github.lolens.inno3.entity;

import io.github.lolens.inno3.exception.WarehouseException;

import java.util.Stack;

public class Warehouse {
  private final int capacity;
  private int cargoCount;

  public Warehouse(int capacity, int cargoCount) {
    this.capacity = capacity;
    this.cargoCount = cargoCount;
  }

  public boolean isFull() {
    return cargoCount >= capacity;
  }

  public boolean isEmpty() {
    return cargoCount <= 0;
  }

  public void add() throws WarehouseException {
    if (isFull()) {
      throw new WarehouseException("Warehouse is full, can't put more cargo in.");
    }
    cargoCount++;
  }

  public void remove() throws WarehouseException {
    if (isEmpty()) {
      throw new WarehouseException("Warehouse is empty, can't give more cargo.");
    }
    cargoCount--;
  }

  public int getCargoCount() {
    return cargoCount;
  }

  public int getCapacity() {
    return capacity;
  }
}
