package by.antonov.multithreading.entity.shipstate;

import by.antonov.multithreading.entity.Ship;

public class WaitingState extends ShipState {

  public WaitingState() {
    super();
  }

  @Override
  public ShipState changeState(Ship ship) {
    ShipState shipState;
    if (ship.mustBeUnloaded() | ship.mustBeUnloaded()) {
      shipState = new MoorState();
    } else {
      shipState = new LeavedState();
    }

    return shipState;
  }
}
