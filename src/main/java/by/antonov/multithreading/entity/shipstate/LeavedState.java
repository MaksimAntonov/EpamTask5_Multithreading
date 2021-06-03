package by.antonov.multithreading.entity.shipstate;

import by.antonov.multithreading.entity.Ship;

public class LeavedState extends ShipState {

  public LeavedState() {
    super(true);
  }

  @Override
  public ShipState changeState(Ship ship) {
    return null;
  }
}
