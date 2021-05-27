package by.antonov.multithreading.entity;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Ship implements Callable<Boolean> {
  private final static Logger logger = LogManager.getLogger();
  private final static Integer MAX_CONTAINER_CAPACITY = 10;
  private final int shipId;
  private int containersCount;

  public Ship(int shipId, int containersCount) {
    logger.info(String.format("Create Ship id=%d with %d containers.", shipId, containersCount));
    this.shipId = shipId;
    this.containersCount = containersCount;
  }

  public void increaseContainersCount() {
    ++containersCount;
    //logger.debug(String.format("++Containers in Ship id=%d: %d", this.shipId, this.containersCount));
  }

  public void decreaseContainersCount() {
    --containersCount;
    //logger.debug(String.format("--Containers in Ship id=%d: %d", this.shipId, this.containersCount));
  }

  @Override
  public Boolean call()
      throws Exception {
    logger.info(String.format("[%s]Ship with id=%d docking to pier.", Thread.currentThread().getName(), this.shipId));
    TimeUnit.SECONDS.sleep(5);

    logger.info(String.format("[%s]Unloading ship with id=%d.", Thread.currentThread().getName(), this.shipId));

    Port port = Port.getInstance();
    while (containersCount > 0) {
      TimeUnit.SECONDS.sleep(2);
      if (port.increaseContainersCount()) {
        decreaseContainersCount();
      }
    }
    TimeUnit.SECONDS.sleep(3);
    logger.info(String.format("[%s]Loading ship with id=%d.", Thread.currentThread().getName(), this.shipId));
    while (containersCount < MAX_CONTAINER_CAPACITY) {
      TimeUnit.SECONDS.sleep(2);
      if (port.decreaseContainersCount()) {
        increaseContainersCount();
      }
    }

    TimeUnit.SECONDS.sleep(5);
    logger.info(String.format("[%s]Ship with id=%d leaving pier.", Thread.currentThread().getName(), this.shipId));
    return null;
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
