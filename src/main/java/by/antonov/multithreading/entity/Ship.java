package by.antonov.multithreading.entity;

import java.util.Optional;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Ship implements Callable<Ship> {
  private static final Logger logger = LogManager.getLogger();
  public static final Integer MAX_CONTAINER_CAPACITY = 10;
  private final int shipId;
  private int containersCount;

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
      Optional<Pier> pierOptional = port.getPier();
      if (pierOptional.isPresent()) {
        Pier pier = pierOptional.get();
        logger.info(String.format("[%s]Ship id=%d on pier #%d",
                                  Thread.currentThread().getName(),
                                  shipId,
                                  pier.getId()));

        pier.handle(this);

        logger.info(String.format("[%s]Ship id=%d leaving pier #%d", Thread.currentThread().getName(), shipId,
                                  pier.getId()));
        port.releasePier(pier);
      }
    } catch (InterruptedException e) {
      logger.error(String.format("Ship.call() id=%d InterruptedException. Thread: %s Message: %s",
                                 shipId, Thread.currentThread().getName(), e.getMessage()));
      Thread.currentThread().interrupt();
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
