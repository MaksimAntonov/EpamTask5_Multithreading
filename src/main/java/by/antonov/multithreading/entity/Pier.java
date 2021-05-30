package by.antonov.multithreading.entity;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Pier {
  private final static Logger logger = LogManager.getLogger();
  private final Integer id;

  public Pier(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void handle(Ship ship) {
    Port port = Port.getInstance();
    try {
      logger.info(String.format("[%s]Unloading ship with id=%d.", Thread.currentThread().getName(), ship.getShipId()));
      while (ship.getContainersCount() > 0) {
        TimeUnit.SECONDS.sleep(2);
        port.increaseContainersCount();
        ship.decreaseContainersCount();
      }

      logger.info(String.format("[%s]Loading ship with id=%d.", Thread.currentThread().getName(), ship.getShipId()));
      while (ship.getContainersCount() < Ship.MAX_CONTAINER_CAPACITY) {
        TimeUnit.SECONDS.sleep(2);
        port.decreaseContainersCount();
        ship.increaseContainersCount();
      }
      logger.info(String.format("[%s]Ship with id=%d handled.", Thread.currentThread().getName(), ship.getShipId()));
    } catch (InterruptedException e) {
      logger.error(String.format("Pier InterruptedException. Thread: %s Message: %s",
                                 Thread.currentThread().getName(), e.getMessage()));
      Thread.currentThread().interrupt();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (getClass() != o.getClass()) {
      return false;
    }

    Pier pier = (Pier) o;
    return id.equals(pier.getId());
  }

  @Override
  public int hashCode() {
    int result = logger != null ? logger.hashCode() : 0;
    result = 31 * result + (id != null ? id.hashCode() : 0);
    return result;
  }
}
