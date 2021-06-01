package by.antonov.multithreading.entity.shipstate;

import by.antonov.multithreading.entity.Port;
import by.antonov.multithreading.entity.Ship;
import java.util.concurrent.TimeUnit;

public class LoadingState extends ShipState {
  @Override
  public ShipState changeState() {
    return new UnmoorState();
  }

  @Override
  public void operation(Ship ship) {
    if (ship.isMoored()) {
      Port port = Port.getInstance();
      try {
        logger.info(String.format("[%s]Loading ship with id=%d.", Thread.currentThread().getName(), ship.getShipId()));
        while (ship.getContainersCount() < Ship.MAX_CONTAINER_CAPACITY) {
          TimeUnit.SECONDS.sleep(2);
          port.decreaseContainersCount();
          ship.increaseContainersCount();
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
