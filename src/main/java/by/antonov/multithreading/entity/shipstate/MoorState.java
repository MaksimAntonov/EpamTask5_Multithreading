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
  public ShipState changeState() {
    return new UnloadingState();
  }

  @Override
  public void operation(Ship ship) {
    Port port = Port.getInstance();
    Optional<Pier> pierOpt = port.getPier();
    if (pierOpt.isPresent()) {
      Pier pier = pierOpt.get();
      ship.setPier(pier);
      logger.info(String.format("[%s]Ship id=%d on pier #%d",
                                Thread.currentThread().getName(),
                                ship.getShipId(),
                                pier.getId()));
    }
  }
}
