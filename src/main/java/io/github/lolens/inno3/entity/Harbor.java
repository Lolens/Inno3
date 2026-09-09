package io.github.lolens.inno3.entity;

import io.github.lolens.inno3.exception.DockException;
import io.github.lolens.inno3.exception.WarehouseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Harbor {
  private static final Logger logger = LoggerFactory.getLogger(Harbor.class);
  private static final Harbor INSTANCE = new Harbor();

  private static final int DOCK_COUNT = 2;
  private static final int WAREHOUSE_CAPACITY = 1000;
  private static final int WAREHOUSE_INITIAL_CARGO = 200;

  private final Warehouse warehouse;
  private final List<Dock> docks;

  private final ReentrantLock dockLock = new ReentrantLock(true);
  private final Condition dockAvailable = dockLock.newCondition();

  private final ReentrantLock warehouseLock = new ReentrantLock(true);
  private final Condition warehouseHasSpace = warehouseLock.newCondition();
  private final Condition warehouseHasCargo = warehouseLock.newCondition();

  private Harbor() {
    List<Dock> toAdd = new ArrayList<>();
    for (int i = 0; i < DOCK_COUNT; i++) {
      toAdd.add(new Dock(i));
    }
    this.docks = toAdd;
    this.warehouse = new Warehouse(WAREHOUSE_CAPACITY, WAREHOUSE_INITIAL_CARGO);
  }

  public static Harbor getInstance() {
    return INSTANCE;
  }

  public Dock dockShip(Ship ship) throws InterruptedException {
    dockLock.lock();
    try {
      while (true) {
        for (Dock dock : docks) {
          if (dock.isFree()) {
            dock.occupy(ship);
            ship.dockTo(dock);
            return dock;
          }
        }
        dockAvailable.await();
      }
    } catch (DockException e) {
      logger.error("Tried docking ship to an already full dock");
      throw new RuntimeException(e);
    } finally {
      dockLock.unlock();
    }
  }

  public void undockShip(Ship ship) {
    dockLock.lock();
    try {
      Dock dock = ship.undock();
      dock.release();
      dockAvailable.signalAll();
    } catch (DockException e) {
      logger.error("Tried undocking ship from an empty dock");
      throw new RuntimeException(e);
    } finally {
      dockLock.unlock();
    }
  }

  public void unloadToWarehouse(Ship ship) throws InterruptedException {
    warehouseLock.lock();
    try {
      while (warehouse.isFull()) {
        warehouseHasSpace.await();
      }
      ship.remove();
      warehouse.add();
      warehouseHasCargo.signalAll();
    } catch (WarehouseException e) {
      logger.error("Tried adding cargo to a warehouse, which was full");
      throw new RuntimeException(e);
    } finally {
      warehouseLock.unlock();
    }
  }

  public void loadFromWarehouse(Ship ship) throws InterruptedException {
    warehouseLock.lock();
    try {
      while (warehouse.isEmpty()) {
        warehouseHasCargo.await();
      }
      warehouse.remove();
      ship.add();
      warehouseHasSpace.signalAll();

    } catch (WarehouseException e) {
      logger.error("Tried removing cargo from a warehouse, which was empty");
      throw new RuntimeException(e);

    } finally {
      warehouseLock.unlock();
    }
  }

}
