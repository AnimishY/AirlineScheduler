import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Distance {
    String origin;
    String destination;
    String duration;

    public Distance(String origin, String destination, String duration) {
        this.origin = origin;
        this.destination = destination;
        this.duration = duration;
    }

    public static ArrayList<Distance> loadDistance(){
        ArrayList<Distance> distanceMap = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader("timeDistance.csv"))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Distance Distance = new Distance(values[0], values[1], values[2]);
                distanceMap.add(Distance);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return distanceMap;

    }
}
