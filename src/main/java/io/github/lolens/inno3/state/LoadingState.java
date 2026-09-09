package io.github.lolens.inno3.state;

import io.github.lolens.inno3.entity.Harbor;
import io.github.lolens.inno3.entity.Ship;

import java.util.concurrent.TimeUnit;

public class LoadingState implements ShipState{
  @Override
  public ShipState advance(Ship ship, Harbor harbor) throws InterruptedException {
    for (int i = 0; i < ship.getToLoad(); i++) {
      if (ship.getCurrentLoad() < ship.getCapacity()) {
        harbor.loadFromWarehouse(ship);
        TimeUnit.MILLISECONDS.sleep(50);
      }
    }
    return new DepartingState();
  }

  @Override
  public String getName() {
    return "LOADING";
  }

  @Override
  public boolean hasNext() {
    return true;
  }
}
