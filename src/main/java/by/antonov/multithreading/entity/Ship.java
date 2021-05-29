package by.antonov.multithreading.entity;

import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Ship implements Callable<Ship> {
  private final static Logger logger = LogManager.getLogger();
  private final int shipId;
  private int containersCount;
  public final static Integer MAX_CONTAINER_CAPACITY = 10;

  public Ship(int shipId, int containersCount) {
    logger.info(String.format("Create Ship id=%d with %d containers.", shipId, containersCount));
    this.shipId = shipId;
    this.containersCount = containersCount;
  }

  public int getShipId() {
    return shipId;
  }

  public int getContainersCount() {
    return containersCount;
  }

  public void increaseContainersCount() {
    ++containersCount;
  }

  public void decreaseContainersCount() {
    --containersCount;
  }

  @Override
  public Ship call() {
    Port port = Port.getInstance();
    try {
      Pier pier = port.getPier();
      logger.info(String.format("[%s]Ship id=%d on pier #%d", Thread.currentThread().getName(), shipId, pier.getId()));

      pier.handle(this);

      logger.info(String.format("[%s]Ship id=%d leaving pier #%d", Thread.currentThread().getName(), shipId,
                                pier.getId()));
      port.setPier(pier);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (getClass() != o.getClass()) {
      return false;
    }

    Ship ship = (Ship) o;
    return (shipId == ship.shipId && containersCount == ship.containersCount);
  }

  @Override
  public int hashCode() {
    int result = shipId;
    result = 31 * result + containersCount;
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Ship{");
    sb.append("shipId=").append(shipId);
    sb.append(", containersCount=").append(containersCount);
    sb.append('}');
    return sb.toString();
  }
}
