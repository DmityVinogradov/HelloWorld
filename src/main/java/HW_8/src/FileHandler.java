package HW_8.src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public static List<Car> readCarsFromFile(String filename) throws IOException {
        List<Car> cars = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.contains("[")) continue;

                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String number = parts[0].trim();
                    String model = parts[1].trim();
                    String color = parts[2].trim();
                    long mileage = Long.parseLong(parts[3].trim());
                    double cost = Double.parseDouble(parts[4].trim());

                    cars.add(new Car(number, model, color, mileage, cost));
                }
            }
        }

        return cars;
    }

    public static void writeResultsToFile(String filename, String results) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(results);
        }
    }
}