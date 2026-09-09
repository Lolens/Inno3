package io.github.lolens.inno3.state;

import io.github.lolens.inno3.entity.Harbor;
import io.github.lolens.inno3.entity.Ship;

import java.util.concurrent.TimeUnit;

public class DockedState implements ShipState {
  @Override
  public ShipState advance(Ship ship, Harbor harbor) throws InterruptedException {
    for (int i = 0; i < ship.getToUnload(); i++) {
      if (ship.getCurrentLoad() > 0) {
        harbor.unloadToWarehouse(ship);
        TimeUnit.MILLISECONDS.sleep(10);
      }
    }
    return new LoadingState();
  }

  @Override
  public String getName() {
    return "UNLOADING";
  }

  @Override
  public boolean hasNext() {
    return true;
  }
}
