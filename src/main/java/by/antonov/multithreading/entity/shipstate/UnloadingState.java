package by.antonov.multithreading.entity.shipstate;

import by.antonov.multithreading.entity.Port;
import by.antonov.multithreading.entity.Ship;
import java.util.concurrent.TimeUnit;

public class UnloadingState extends ShipState {

  public UnloadingState() {
    super();
  }

  @Override
  public ShipState changeState() {
    return new LoadingState();
  }

  @Override
  public void operation(Ship ship) {
    if (ship.isMoored()) {
      Port port = Port.getInstance();
      try {
        logger.info(String.format("[%s]Unloading ship with id=%d.", Thread.currentThread().getName(),
                                  ship.getShipId()));
        while (ship.getContainersCount() > 0) {
          TimeUnit.SECONDS.sleep(2);
          port.increaseContainersCount();
          ship.decreaseContainersCount();
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
