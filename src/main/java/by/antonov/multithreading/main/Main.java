package by.antonov.multithreading.main;

import by.antonov.multithreading.entity.Port;
import by.antonov.multithreading.entity.Ship;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {

  public static void main(String[] args) {
    Gson gson = new Gson();
    Port port = Port.getInstance();

    InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("data/ships.json");
    if (inputStream != null) {
      JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonArray()
                .forEach(ship -> Port.getInstance().addShipToQueue(gson.fromJson(ship, Ship.class)));
      port.startOperation();
    }
  }
}
