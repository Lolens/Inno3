package io.github.lolens.inno3.state;

import io.github.lolens.inno3.entity.Harbor;
import io.github.lolens.inno3.entity.Ship;

public class DepartedState implements ShipState{
  @Override
  public ShipState advance(Ship ship, Harbor harbor) throws InterruptedException {
    return this;
  }

  @Override
  public String getName() {
    return "DEPARTED";
  }

  @Override
  public boolean hasNext() {
    return false;
  }
}
