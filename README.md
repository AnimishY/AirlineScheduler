### Air-Scheduler: README

---

## **Overview**

Air-Scheduler is a Java-based scheduling tool designed to allocate available aircraft (referred to as *equipment*) to predefined flight routes in a manner that maximizes efficiency and ensures optimal resource utilization. The program reads input data from CSV files for equipment, routes, and distances between airports, processes the data, and generates a detailed schedule exported to a CSV file.

---

## **Capabilities**

1. **Dynamic Aircraft Assignment**  
   - Assigns available aircraft to scheduled flight routes based on availability, proximity, and departure time.
   - Includes support for ferry flights to reposition aircraft to the origin of a flight when no equipment is locally available.

2. **Conflict-Free Scheduling**  
   - Ensures no aircraft is double-booked by managing its availability using a time-based system.

3. **CSV Export for Analysis**  
   - Outputs a detailed schedule in a structured CSV format, showing each aircraft's timeline for 24 hours.

4. **Input Data Flexibility**  
   - Reads data for aircraft, routes, and distances from easily editable CSV files:
     - `equipment.csv`: List of aircraft and their base location.
     - `routes.csv`: Scheduled flights with origin, destination, departure time, and flight duration.
     - `timeDistance.csv`: Flight times between airports for ferry operations.

5. **Error Handling**  
   - Handles missing or incomplete data gracefully with appropriate log outputs for debugging.

---

## **How It Works**

### **Algorithm and General Logic**

1. **Data Loading**
   - Equipment, routes, and distances are read from respective CSV files into memory as structured `ArrayList` objects.

2. **Route Sorting**
   - Flight routes are sorted by departure times to ensure the scheduling logic processes them in chronological order.

3. **Aircraft Assignment**
   - For each route:
     - An available aircraft already at the origin airport is selected if it meets the departure time constraints.
     - If no suitable aircraft is available at the origin, the algorithm searches for a ferryable aircraft from another airport. The ferry duration and departure time constraints are calculated using `timeDistance.csv`.

4. **Schedule Management**
   - Each aircraft’s availability is updated dynamically after each flight, including ferry flights. 
   - The schedule is stored in a time-slot format for export to CSV.

5. **Export**
   - The completed schedule is written to `AirScheduler.csv`, showing each aircraft's assigned tasks for every hour.

---

### **Analysis of the Algorithm**

#### **Strengths**
1. **Greedy Approach for Scheduling**
   - The algorithm attempts to assign the first available and closest equipment to flights, ensuring simplicity and speed.
   
2. **Conflict-Free Resource Allocation**
   - Time-based checks ensure no aircraft is overbooked, providing a robust scheduling mechanism.

3. **Efficiency**
   - The chronological route sorting reduces unnecessary complexity during scheduling, as each flight is processed in order.

#### **Limitations**
1. **Global Optimization**
   - The algorithm focuses on local optimization (greedy strategy) and does not consider the overall impact of a single decision on future scheduling.
   
2. **No Backtracking**
   - Once an aircraft is assigned or a ferry is planned, the decision is final. There is no reevaluation to optimize for future routes.

3. **Time Granularity**
   - The scheduling considers flight and ferry times to the minute, but there may be edge cases where simultaneous activities could theoretically occur.

---

## **Purpose and Need**

Air-Scheduler was developed to solve real-world challenges in aircraft management, particularly in situations with:
- **Limited Resources**: Optimizing the allocation of a fixed fleet to a large number of flight routes.
- **Dynamic Requirements**: Managing changing flight schedules while minimizing downtime.
- **Operational Complexity**: Automating manual scheduling processes to save time and reduce errors.

This tool can be used by airlines, air cargo services, or logistics companies that require precise and efficient scheduling of their fleets.

---

## **Input Files**

### 1. `equipment.csv`
- Format: `TailSign,Type,BaseAirport`
- Example:
  ```
  EQ123,Airbus A320,DEL
  EQ124,Boeing 737,BOM
  ```

### 2. `routes.csv`
- Format: `FlightNumber,Origin,Destination,DepartureTime,Duration`
- Example:
  ```
  AI101,DEL,BOM,0800,0200
  AI102,BOM,DEL,1200,0200
  ```

### 3. `timeDistance.csv`
- Format: `Origin,Destination,Duration`
- Example:
  ```
  DEL,BOM,0200
  BOM,DEL,0200
  ```

---

## **Output**

### `AirScheduler.csv`
- Format:
  ```
  Equipment,0000,0100,0200,...,2300
  EQ123,Ferry to BOM,AI101: DEL -> BOM (Landing: 1000),,,
  EQ124,,AI102: BOM -> DEL (Landing: 1400),,,
  ```

---

## **How to Run**

1. Compile and run the Java program using:
   ```bash
   javac Main.java
   java Main
   ```
2. Ensure the input files (`equipment.csv`, `routes.csv`, `timeDistance.csv`) are in the same directory as the program.
3. The program outputs logs to the console and creates the `AirScheduler.csv` file in the current directory.

---

## **Future Enhancements**

1. **Improved Optimization**  
   - Implementing a backtracking or dynamic programming approach to ensure global optimization across the entire schedule.

2. **GUI Integration**  
   - Building a user-friendly graphical interface for managing inputs and visualizing the output schedule.

3. **Real-Time Updates**  
   - Adding capabilities to dynamically update the schedule when new routes or equipment data becomes available.

4. **Error Handling**  
   - Expanding validations for missing or malformed CSV data.

5. **Multi-Day Scheduling**  
   - Extending the algorithm to manage schedules over multiple days.

---

## **Conclusion**

Air-Scheduler demonstrates how efficient resource management can be achieved using structured programming techniques. It provides a robust foundation for more complex scheduling systems while delivering functional and reliable results for small-to-medium operations.
