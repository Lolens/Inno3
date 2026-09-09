package io.github.lolens.inno3.state;

import io.github.lolens.inno3.entity.Harbor;
import io.github.lolens.inno3.entity.Ship;

public interface ShipState {

  ShipState advance(Ship ship, Harbor harbor) throws InterruptedException;

  String getName();

  boolean hasNext();

}
