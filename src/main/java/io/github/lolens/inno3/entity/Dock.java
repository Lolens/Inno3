package io.github.lolens.inno3.entity;

public class Dock {

  private final int id;
  /// -1 if not occupied
  private int occupiedByShipId = -1;

  public Dock(int id) {
    this.id = id;
  }

  public boolean isFree() {
    return occupiedByShipId == -1;
  }

  public void occupy(Ship ship) {
    this.occupiedByShipId = ship.getId();
  }

  public void release() {
    this.occupiedByShipId = -1;
  }

  public int getOccupiedByShipId() {
    return occupiedByShipId;
  }


}
