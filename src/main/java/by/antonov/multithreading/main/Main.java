package by.antonov.multithreading.main;

import by.antonov.multithreading.entity.Port;
import by.antonov.multithreading.entity.Ship;
import by.antonov.multithreading.exception.PortException;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {

  public static void main(String[] args)
      throws PortException {
    Port port = Port.getInstance();
    Gson gson = new Gson();
    InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("data/ships.json");
    if (inputStream != null) {
      JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonArray()
                .forEach(ship -> Port.getInstance().addShipToQueue(gson.fromJson(ship, Ship.class)));
      port.handle();
    }
  }

  public static void test() {
    Gson gson = new Gson();
    InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("data/ships.json");

    if (inputStream != null) {
      JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonArray().forEach(
          ship -> {
            Port.getInstance().addShipToQueue(gson.fromJson(ship, Ship.class));
          }
      );
      System.out.println('a');
      //BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      //List<Ship> ships =
      //reader.lines().forEach(json -> System.out.println(json.getClass().getName()));
      //System.out.println(ships);
    }
  }
}
