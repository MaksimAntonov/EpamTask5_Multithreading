package by.antonov.multithreading.entity.shipstate;

import by.antonov.multithreading.entity.Port;
import by.antonov.multithreading.entity.Ship;
import java.util.concurrent.TimeUnit;

public class UnloadingState extends ShipState {

  public UnloadingState() {
    super();
  }

  @Override
  public ShipState changeState(Ship ship) {
    ShipState shipState;
    if (ship.mustBeLoaded()) {
      shipState = new LoadingState();
    } else {
      shipState = new UnmoorState();
    }
    return shipState;
  }

  @Override
  public boolean operation(Ship ship) {
    boolean result = false;
    if (ship.isMoored()) {
      Port port = Port.getInstance();
      try {
        logger.info("Unloading ship {}", ship);
        while (ship.getContainersCount() > ship.unloadingContainers()) {
          TimeUnit.SECONDS.sleep(2);
          port.increaseContainersCount();
          ship.decreaseContainersCount();
        }
        result = true;
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }

    return result;
  }
}
