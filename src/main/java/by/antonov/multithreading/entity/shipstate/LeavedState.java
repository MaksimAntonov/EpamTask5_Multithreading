package by.antonov.multithreading.entity.shipstate;

public class LeavedState extends ShipState {

  public LeavedState() {
    super(true);
  }

  @Override
  public ShipState changeState() {
    return null;
  }
}
