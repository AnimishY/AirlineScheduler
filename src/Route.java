import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Route {
    String flightNumber;
    String origin;
    String destination;
    String departureTime;
    String duration;

    public Route(String flightNumber, String origin, String destination, String departureTime, String duration) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.duration = duration;
    }

    public static ArrayList<Route> loadRoutes() {
        ArrayList<Route> routeList = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader("routes.csv"))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Route route = new Route(values[0], values[1], values[2], values[3], values[4]);
                routeList.add(route);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return routeList;
    }
}
