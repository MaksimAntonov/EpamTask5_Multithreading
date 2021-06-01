package by.antonov.multithreading.entity.shipstate;

public class WaitingState extends ShipState {

  public WaitingState() {
    super();
  }

  @Override
  public ShipState changeState() {
    return new MoorState();
  }
}
