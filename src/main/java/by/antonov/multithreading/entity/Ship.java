package by.antonov.multithreading.entity;

import by.antonov.multithreading.entity.shipstate.ShipState;
import by.antonov.multithreading.entity.shipstate.WaitingState;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Ship implements Callable<Ship> {
  private static final Logger logger = LogManager.getLogger();
  public static final Integer MAX_CONTAINER_CAPACITY = 10;
  private ShipState shipState;
  private Pier pier;
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

  public void setPier(Pier pier) {
    if (pier != null) {
      this.pier = pier;
    }
  }

  public Pier releasePier() {
    Pier pier = this.pier;
    this.pier = null;
    return pier;
  }

  public Boolean isMoored() {
    return this.pier != null;
  }

  public void operation() {
    shipState.operation(this);
    shipState = shipState.changeState();
  }

  @Override
  public Ship call() {
    this.shipState = new WaitingState();
    do {
      this.operation();
    } while (!shipState.isLastState());

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
