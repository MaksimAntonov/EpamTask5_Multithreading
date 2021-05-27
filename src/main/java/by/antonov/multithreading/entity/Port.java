package by.antonov.multithreading.entity;

import by.antonov.multithreading.exception.PortException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Port {
  private final static Logger logger = LogManager.getLogger();
  private static Port instance;
  private final static int PIERS_COUNT;
  private final static int CONTAINER_CAPACITY;
  private final static int CONTAINER_MAX_COUNT;
  private final static int CONTAINER_MIN_COUNT;
  private static int containerCount;
  private final Queue<Ship> shipsInQueue = new ArrayDeque<>();

  static {
    Properties properties = new Properties();
    try {
      InputStream inputStream = Port.class.getClassLoader().getResourceAsStream("data/Po2rt.properties");
      if (inputStream != null) {
        properties.load(inputStream);
      }
    } catch (IOException e) {
      logger.error(String.format("Can not read properties from file. %s", e.getMessage()));
    }
    PIERS_COUNT = Integer.parseInt(properties.getProperty("PiersCount", "3"));
    CONTAINER_CAPACITY = Integer.parseInt(properties.getProperty("ContainerCapacity", "25"));
    CONTAINER_MAX_COUNT = Integer.parseInt(properties.getProperty("ContainerMaxCount", "21"));
    CONTAINER_MIN_COUNT = Integer.parseInt(properties.getProperty("ContainerMinCount", "5"));
    containerCount = Integer.parseInt(properties.getProperty("ContainerCount", "21"));
  }

  private Port() {
  }

  public static Port getInstance() {
    if (instance == null) {
      instance = new Port();
    }

    return instance;
  }

  public void addShipToQueue(Ship ship) {
    shipsInQueue.add(ship);
  }

  public Boolean increaseContainersCount() {
    boolean result = false;
    if (containerCount < CONTAINER_CAPACITY) {
      ++containerCount;
      if (containerCount > CONTAINER_MAX_COUNT) {
        containerCount = (int) (CONTAINER_CAPACITY * 0.75);
      }
      result = true;
    }

    return result;
  }

  public Boolean decreaseContainersCount() {
    boolean result = false;
    if (containerCount > 0) {
      --containerCount;
      if (containerCount < CONTAINER_MIN_COUNT) {
        containerCount = (int) (CONTAINER_CAPACITY * 0.75);;
      }
      result = true;
    }

    return result;
  }

  public void handle()
      throws PortException {
    ExecutorService service = Executors.newFixedThreadPool(PIERS_COUNT);
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
      logger.error(String.format("Port error. Message: %s", e.getMessage()));
      throw new PortException(String.format("Port error. Message: %s", e.getMessage()));
    }
  }
}
