import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ArrayList<Equipment> equipmentList = new ArrayList<>();
        ArrayList<Route> routeList = new ArrayList<>();
        ArrayList<Distance> distanceList = new ArrayList<>();

        System.out.println("Welcome to Air-Scheduler");

        equipmentList = Equipment.loadEquipment();
        routeList = Route.loadRoutes();
        distanceList = Distance.loadDistance();

        System.out.println("Equipment loaded: " + equipmentList.size());
        System.out.println("Routes loaded: " + routeList.size());
        System.out.println("Distances loaded: " + distanceList.size());

        // Sort the routes list by departure time
        routeList.sort(Comparator.comparing(r -> r.departureTime));

        // Schedule map to export to CSV
        Map<String, Map<String, String>> scheduleMap = new LinkedHashMap<>();

        // Initialize the schedule map with equipment
        for (Equipment equipment : equipmentList) {
            scheduleMap.put(equipment.tailSign, new LinkedHashMap<>());
        }

        for (Route route : routeList) {
            Equipment equipment = Equipment.getAvailableEquipment(route.origin, route.departureTime, equipmentList);

            if (equipment == null) {
                equipment = Equipment.getFerryEquipment(route.origin, route.departureTime, equipmentList, distanceList);
                if (equipment != null) {
                    String ferryTime = Equipment.getFerryTime(equipment.currentAirport, route.origin, distanceList);
                    System.out.println("Ferry flight for equipment " + equipment.tailSign + " from " + equipment.currentAirport + " to " + route.origin + " at " + ferryTime + " for " + route.flightNumber + " at " + route.departureTime);
                    equipment.updateLocation(route.origin, route.departureTime, ferryTime);

                    // Log ferry flight into schedule map
                    scheduleMap.get(equipment.tailSign).put(ferryTime, "Ferry to " + route.origin);
                }
            }

            if (equipment != null) {
                String landingTime = equipment.addTime(route.departureTime, route.duration);
                System.out.println("Scheduled flight for equipment " + equipment.tailSign + " for " + route.flightNumber + " from " + route.origin + " to " + route.destination + " at " + route.departureTime + " (Landing at " + landingTime + ")");
                equipment.updateLocation(route.destination, route.departureTime, route.duration);

                // Log scheduled flight into schedule map
                scheduleMap.get(equipment.tailSign).put(route.departureTime, route.flightNumber + ": " + route.origin + " -> " + route.destination + " (Landing: " + landingTime + ")");
            } else {
                System.out.println("No available equipment for route from " + route.origin + " to " + route.destination + " at " + route.departureTime);
            }
        }

        // Export the schedule map to a CSV file
        exportToCsv(scheduleMap);
    }

    public static void exportToCsv(Map<String, Map<String, String>> scheduleMap) {
        try (FileWriter writer = new FileWriter("AirScheduler.csv")) {
            // Write header row with time slots
            writer.append("Equipment");
            for (int i = 0; i < 24; i++) {
                writer.append(",").append(String.format("%02d00", i));
            }
            writer.append("\n");

            // Write schedule for each equipment
            for (Map.Entry<String, Map<String, String>> entry : scheduleMap.entrySet()) {
                String equipment = entry.getKey();
                Map<String, String> schedule = entry.getValue();

                writer.append(equipment);
                for (int i = 0; i < 24; i++) {
                    String timeSlot = String.format("%02d00", i);
                    writer.append(",").append(schedule.getOrDefault(timeSlot, ""));
                }
                writer.append("\n");
            }

            System.out.println("Schedule exported to AirScheduler.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
