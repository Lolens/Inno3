package io.github.lolens.inno3.state;

import io.github.lolens.inno3.entity.Harbor;
import io.github.lolens.inno3.entity.Ship;

public class DepartingState implements ShipState{
  @Override
  public ShipState advance(Ship ship, Harbor harbor) throws InterruptedException {
    harbor.undockShip(ship);

    return new DepartedState();
  }

  @Override
  public String getName() {
    return "DEPARTING";
  }

  @Override
  public boolean hasNext() {
    return true;
  }
}
