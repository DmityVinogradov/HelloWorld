package HW_8.src.service;

import HW_8.src.model.Car;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public static List<Car> readCarsFromFile(String filename) throws IOException {
        List<Car> cars = new ArrayList<>();

        File file = new File("src/data/" + filename);
        if (!file.exists()) {
            file = new File("data/" + filename);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.contains("[") || line.contains("Номер")) {
                    continue; // Пропускаем заголовки и пустые строки
                }

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
        // Создаем папку data если ее нет
        File dataDir = new File("src/data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        File file = new File("src/data/" + filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(results);
        }
    }
}