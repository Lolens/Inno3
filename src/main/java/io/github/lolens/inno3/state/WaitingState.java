package io.github.lolens.inno3.state;

import io.github.lolens.inno3.entity.Dock;
import io.github.lolens.inno3.entity.Harbor;
import io.github.lolens.inno3.entity.Ship;

public class WaitingState implements ShipState{
  @Override
  public ShipState advance(Ship ship, Harbor harbor) throws InterruptedException {
    Dock dock = harbor.dockShip(ship);
    ship.dockTo(dock);
    return new DockedState();
  }

  @Override
  public String getName() {
    return "WAITING";
  }

  @Override
  public boolean isLast() {
    return false;
  }
}
