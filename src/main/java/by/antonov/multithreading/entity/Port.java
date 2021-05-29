package by.antonov.multithreading.entity;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Port {
  private final static Logger logger = LogManager.getLogger();
  private static Port instance;
  private final static AtomicBoolean isInitialized = new AtomicBoolean(false);
  private static final int PIERS_COUNT;
  private static final int CONTAINER_CAPACITY;
  private static final int CONTAINER_MAX_COUNT;
  private static final int CONTAINER_MIN_COUNT;
  private static int containerCount;
  private final Queue<Ship> shipsInQueue = new ArrayDeque<>();
  private final Queue<Pier> pierQueue = new ArrayDeque<>();
  private final ReentrantLock pierQueueLocker = new ReentrantLock();
  private final ReentrantLock containerOperationLocker = new ReentrantLock();
  private final Condition pierQueueCondition = pierQueueLocker.newCondition();

  static {
    try {
      ResourceBundle resourceBundle = ResourceBundle.getBundle("data/Port");
      PIERS_COUNT = Integer.parseInt(resourceBundle.getString("PiersCount"));
      CONTAINER_CAPACITY = Integer.parseInt(resourceBundle.getString("ContainerCapacity"));
      CONTAINER_MAX_COUNT = (int) (CONTAINER_CAPACITY * 0.9);
      CONTAINER_MIN_COUNT = (int) (CONTAINER_CAPACITY * 0.2);
      containerCount = Integer.parseInt(resourceBundle.getString("ContainerCount"));
    } catch (Exception e) {
      logger.fatal(String.format("Port initialize error. Message: %s", e.getMessage()));
      throw new ExceptionInInitializerError(String.format("Port initialize error. Message: %s", e.getMessage()));
    }
  }

  private Port() {
    for (int i = 0; i < PIERS_COUNT; i++){
      pierQueue.add(new Pier(i + 1));
    }
  }

  public static Port getInstance() {
    while (instance == null) {
      if (isInitialized.compareAndSet(false, true)) {
        instance = new Port();
      }
    }

    return instance;
  }

  public void addShipToQueue(Ship ship) {
    shipsInQueue.add(ship);
  }

  public Boolean increaseContainersCount() {
    boolean result = false;
    containerOperationLocker.lock();
    if (containerCount < CONTAINER_CAPACITY) {
      ++containerCount;

      if (containerCount > CONTAINER_MAX_COUNT) {
        containerCount = (int) (CONTAINER_CAPACITY * 0.75);
      }
      result = true;
    }
    containerOperationLocker.unlock();
    return result;
  }

  public Boolean decreaseContainersCount() {
    boolean result = false;
    containerOperationLocker.lock();
    if (containerCount > 0) {
      --containerCount;

      if (containerCount < CONTAINER_MIN_COUNT) {
        containerCount = (int) (CONTAINER_CAPACITY * 0.75);
      }
      containerOperationLocker.unlock();
      result = true;
    }

    return result;
  }

  public void startOperation() {
    ExecutorService service = Executors.newFixedThreadPool(shipsInQueue.size());
    while (!shipsInQueue.isEmpty()) {
      service.submit(shipsInQueue.poll());
    }

    service.shutdown();
    try {
      if (service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
        logger.info("All ships was handled.");
      } else {
        logger.info("Termination by timeout.");
      }
    } catch (InterruptedException e) {
      logger.error(String.format("Port operation error. Message: %s", e.getMessage()));
    }
  }

  public Pier getPier()
      throws InterruptedException {
    Pier pier = null;
    pierQueueLocker.lock();
    try {
      while ((pier = pierQueue.poll()) == null) {
        pierQueueCondition.await();
      }

    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    } finally {
      pierQueueLocker.unlock();
    }

    return pier;
  }

  public void setPier(Pier pier) {
    if (pier != null) {
      pierQueueLocker.lock();
      pierQueue.add(pier);
      pierQueueCondition.signal();
      pierQueueLocker.unlock();
    }
  }
}
