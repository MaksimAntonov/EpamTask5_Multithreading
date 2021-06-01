package by.antonov.multithreading.entity;

import java.util.ArrayDeque;
import java.util.Optional;
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

  private static final Logger logger = LogManager.getLogger();
  private static Port instance;
  private static final AtomicBoolean isInitialized = new AtomicBoolean(false);
  private static final int PIERS_COUNT;
  private static final int CONTAINER_CAPACITY;
  private static final int CONTAINER_MAX_COUNT;
  private static final int CONTAINER_MIN_COUNT;
  private static final int CONTAINER_RESET_COUNT;
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
      CONTAINER_MAX_COUNT = Integer.parseInt(resourceBundle.getString("ContainerMaxCount"));
      CONTAINER_MIN_COUNT = Integer.parseInt(resourceBundle.getString("ContainerMinCount"));
      CONTAINER_RESET_COUNT = Integer.parseInt(resourceBundle.getString("ContainerResetCount"));
      containerCount = Integer.parseInt(resourceBundle.getString("ContainerCount"));
    } catch (Exception e) {
      logger.fatal(String.format("Port initialize error. Message: %s", e.getMessage()));
      throw new ExceptionInInitializerError(String.format("Port initialize error. Message: %s", e.getMessage()));
    }
  }

  private Port() {
    for (int i = 0; i < PIERS_COUNT; i++) {
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

  public void increaseContainersCount() {
    containerOperationLocker.lock();
    try {
      if (containerCount < CONTAINER_CAPACITY) {
        ++containerCount;

        if (containerCount > CONTAINER_MAX_COUNT) {
          containerCount = CONTAINER_RESET_COUNT;
        }
      }
    } finally {
      containerOperationLocker.unlock();
    }
  }

  public void decreaseContainersCount() {
    containerOperationLocker.lock();
    try {
      if (containerCount > 0) {
        --containerCount;

        if (containerCount < CONTAINER_MIN_COUNT) {
          containerCount = CONTAINER_RESET_COUNT;
        }
      }
    } finally {
      containerOperationLocker.unlock();
    }
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
      logger.error(String.format("Port.startOperation() InterruptedException. Thread: %s Message: %s",
                                 Thread.currentThread().getName(), e.getMessage()));
      Thread.currentThread().interrupt();
    }
  }

  public Optional<Pier> getPier() {
    Pier pier = null;
    pierQueueLocker.lock();
    try {
      while ((pier = pierQueue.poll()) == null) {
        pierQueueCondition.await();
      }

    } catch (InterruptedException e) {
      logger.error(String.format("Port.getPier() InterruptedException. Thread: %s Message: %s",
                                 Thread.currentThread().getName(), e.getMessage()));
      Thread.currentThread().interrupt();
    } finally {
      pierQueueLocker.unlock();
    }

    return Optional.ofNullable(pier);
  }

  public void releasePier(Pier pier) {
    if (pier != null) {
      pierQueueLocker.lock();
      try {
        pierQueue.add(pier);
        pierQueueCondition.signal();
      } finally {
        pierQueueLocker.unlock();
      }
    }
  }
}
