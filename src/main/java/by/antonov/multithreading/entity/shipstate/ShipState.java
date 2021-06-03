package by.antonov.multithreading.entity.shipstate;

import by.antonov.multithreading.entity.Ship;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ShipState {

  protected static final Logger logger = LogManager.getLogger();
  private final boolean lastState;

  public ShipState() {
    this(false);
  }

  public ShipState(boolean lastState) {
    this.lastState = lastState;
  }

  public boolean isLastState() {
    return this.lastState;
  }

  public boolean operation(Ship ship) {
    return true;
  }

  public abstract ShipState changeState(Ship ship);

}
