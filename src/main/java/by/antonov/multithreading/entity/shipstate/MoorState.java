package by.antonov.multithreading.entity.shipstate;

import by.antonov.multithreading.entity.Pier;
import by.antonov.multithreading.entity.Port;
import by.antonov.multithreading.entity.Ship;
import java.util.Optional;

public class MoorState extends ShipState {

  public MoorState() {
    super();
  }

  @Override
  public ShipState changeState(Ship ship) {
    ShipState shipState;
    if (ship.mustBeUnloaded()) {
      shipState = new UnloadingState();
    } else if (ship.mustBeLoaded()) {
      shipState = new LoadingState();
    } else {
      shipState = new UnmoorState();
    }

    return shipState;
  }

  @Override
  public boolean operation(Ship ship) {
    boolean result = false;
    Port port = Port.getInstance();
    Optional<Pier> pierOpt = port.getPier();
    if (pierOpt.isPresent()) {
      Pier pier = pierOpt.get();
      ship.setPier(pier);
      logger.info("Ship on pier {}", ship);
      result = true;
    }

    return result;
  }
}
