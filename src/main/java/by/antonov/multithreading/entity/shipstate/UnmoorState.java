package by.antonov.multithreading.entity.shipstate;

import by.antonov.multithreading.entity.Pier;
import by.antonov.multithreading.entity.Port;
import by.antonov.multithreading.entity.Ship;

public class UnmoorState extends ShipState {

  public UnmoorState() {
    super();
  }

  @Override
  public ShipState changeState(Ship ship) {
    return new LeavedState();
  }

  @Override
  public boolean operation(Ship ship) {
    Port port = Port.getInstance();
    Pier pier = ship.releasePier();
    port.releasePier(pier);
    logger.info("Ship handled {}", ship);
    return true;
  }
}
