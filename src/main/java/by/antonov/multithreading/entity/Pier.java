package by.antonov.multithreading.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Pier {

  private static final Logger logger = LogManager.getLogger();
  private final Integer id;

  public Pier(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (getClass() != o.getClass()) {
      return false;
    }

    Pier pier = (Pier) o;
    return id.equals(pier.getId());
  }

  @Override
  public int hashCode() {
    int result = logger != null ? logger.hashCode() : 0;
    result = 31 * result + (id != null ? id.hashCode() : 0);
    return result;
  }
}
