package io.github.lolens.inno3.entity;

import io.github.lolens.inno3.exception.DockException;

public class Dock {

  private final int id;
  /// {@code null} if not occupied
  private Ship currentlyOccupiedShip = null;

  public Dock(int id) {
    this.id = id;
  }

  public boolean isFree() {
    return currentlyOccupiedShip == null;
  }

  public void occupy(Ship ship) throws DockException {
    if (this.currentlyOccupiedShip != null) {
      throw new DockException(DockException.DOCK_ALREADY_OCCUPIED);
    }
    this.currentlyOccupiedShip = ship;
  }

  public void release() throws DockException {
    if (this.currentlyOccupiedShip == null) {
      throw new DockException(DockException.DOCK_ALREADY_EMPTY);
    }
    this.currentlyOccupiedShip = null;
  }

  public Ship getShip() {
    return currentlyOccupiedShip;
  }


}
