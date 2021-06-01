package by.antonov.multithreading.entity.shipstate;

import by.antonov.multithreading.entity.Ship;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ShipState {

  protected static final Logger logger = LogManager.getLogger();
  private final Boolean lastState;

  public ShipState() {
    this(false);
  }

  public ShipState(Boolean lastState) {
    this.lastState = lastState;
  }

  public Boolean isLastState() {
    return this.lastState;
  }

  public void operation(Ship ship) {
  }

  public abstract ShipState changeState();

}
