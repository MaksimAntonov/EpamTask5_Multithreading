package by.antonov.multithreading.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Pier {

  private static final Logger logger = LogManager.getLogger();
  private final int id;

  public Pier(int id) {
    this.id = id;
  }

  public int getId() {
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
    return (id == pier.getId());
  }

  @Override
  public int hashCode() {
    int result = logger != null ? logger.hashCode() : 0;
    result = 31 * result + id;
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Pier{");
    sb.append("id=").append(id);
    sb.append('}');
    return sb.toString();
  }
}
