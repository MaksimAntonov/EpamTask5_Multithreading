package by.antonov.multithreading.entity.shipstate;

import by.antonov.multithreading.entity.Port;
import by.antonov.multithreading.entity.Ship;
import java.util.concurrent.TimeUnit;

public class LoadingState extends ShipState {

  @Override
  public ShipState changeState(Ship ship) {
    return new UnmoorState();
  }

  @Override
  public boolean operation(Ship ship) {
    boolean result = false;
    if (ship.isMoored()) {
      Port port = Port.getInstance();
      try {
        logger.info("Loading ship {}", ship);
        while (ship.getContainersCount() < ship.loadingContainers()) {
          TimeUnit.SECONDS.sleep(2);
          port.decreaseContainersCount();
          ship.increaseContainersCount();
        }

        result = true;
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
    return result;
  }
}
