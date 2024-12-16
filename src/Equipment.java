import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Equipment {
    String tailSign;
    String type;
    String currentAirport;
    boolean isAvailable;
    String nextAvailableTime;

    public Equipment(String tailSign, String type, String currentAirport) {
        this.tailSign = tailSign;
        this.type = type;
        this.currentAirport = currentAirport;
        this.isAvailable = true;
        this.nextAvailableTime = "0000"; // Assuming 24-hour format HHMM
    }

    public static ArrayList<Equipment> loadEquipment() {
        ArrayList<Equipment> equipmentList = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader("equipment.csv"))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Equipment equipment = new Equipment(values[0], values[1], values[2]);
                equipmentList.add(equipment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return equipmentList;
    }

    public static Equipment getAvailableEquipment(String origin, String departureTime, ArrayList<Equipment> equipmentList) {
        for (Equipment equipment : equipmentList) {
            if (equipment.isAvailable && equipment.currentAirport.equals(origin) && equipment.nextAvailableTime.compareTo(departureTime) <= 0) {
                return equipment;
            }
        }
        return null;
    }

    public static Equipment getFerryEquipment(String origin, String departureTime, ArrayList<Equipment> equipmentList, ArrayList<Distance> distanceList) {
        for (Equipment equipment : equipmentList) {
            if (equipment.isAvailable && !equipment.currentAirport.equals(origin)) {
                String ferryTime = getFerryTime(equipment.currentAirport, origin, distanceList);
                if (ferryTime != null && equipment.nextAvailableTime.compareTo(ferryTime) <= 0 && ferryTime.compareTo(departureTime) <= 0) {
                    return equipment;
                }
            }
        }
        return null;
    }

    public static String getFerryTime(String currentAirport, String destination, ArrayList<Distance> distanceList) {
        for (Distance distance : distanceList) {
            if (distance.origin.equals(currentAirport) && distance.destination.equals(destination)) {
                return distance.duration;
            }
        }
        return null;
    }

    public void updateLocation(String newLocation, String departureTime, String duration) {
        this.currentAirport = newLocation;
        this.nextAvailableTime = addTime(departureTime, duration);
        this.isAvailable = true; // Reset availability after updating location.
    }


    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    String addTime(String time, String duration) {
        int timeInMinutes = Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(2));
        int durationInMinutes = Integer.parseInt(duration.substring(0, 2)) * 60 + Integer.parseInt(duration.substring(2));
        int newTimeInMinutes = timeInMinutes + durationInMinutes;
        int newHours = newTimeInMinutes / 60;
        int newMinutes = newTimeInMinutes % 60;
        return String.format("%02d%02d", newHours, newMinutes);
    }
}