package HW_8.src.test;

import HW_8.src.service.FileHandler;
import HW_8.src.model.Car;
import HW_8.src.repository.CarsRepositoryImpl;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Чтение данных из файла
            List<Car> cars = FileHandler.readCarsFromFile("Cars.txt");

            // Создание репозитория и добавление автомобилей
            CarsRepositoryImpl repository = new CarsRepositoryImpl();
            for (Car car : cars) {
                repository.addCar(car);
            }

            // Вывод всех автомобилей
            System.out.println("Автомобили в базе:");
            System.out.println("Number  Model     Color   Mileage Cost");
            for (Car car : repository.getAllCars()) {
                System.out.println(car);
            }
            System.out.println();

            // Выполнение заданий с помощью Stream API

            // 1) Номера автомобилей по цвету или пробегу
            String colorToFind = "Black";
            long mileageToFind = 0L;
            List<String> numbers = repository.findNumbersByColorOrMileage(colorToFind, mileageToFind);
            System.out.println("Номера автомобилей по цвету или пробегу: " + String.join(" ", numbers));

            // 2) Количество уникальных моделей в ценовом диапазоне
            double minPrice = 700000;
            double maxPrice = 800000;
            long uniqueModelsCount = repository.countUniqueModelsInPriceRange(minPrice, maxPrice);
            System.out.println("Уникальные автомобили: " + uniqueModelsCount + " шт.");

            // 3) Цвет автомобиля с минимальной стоимостью
            String cheapestColor = repository.findColorOfCheapestCar();
            System.out.println("Цвет автомобиля с минимальной стоимостью: " + cheapestColor);

            // 4) Средняя стоимость моделей
            String modelToFind1 = "Toyota";
            String modelToFind2 = "Volvo";
            double avgCost1 = repository.findAverageCostByModel(modelToFind1);
            double avgCost2 = repository.findAverageCostByModel(modelToFind2);
            System.out.printf("Средняя стоимость модели %s: %.2f\n", modelToFind1, avgCost1);
            System.out.printf("Средняя стоимость модели %s: %.2f\n", modelToFind2, avgCost2);

            // Запись результатов в файл
            StringBuilder results = new StringBuilder();
            results.append("Результаты анализа автомобилей:\n")
                    .append("Номера автомобилей по цвету ").append(colorToFind)
                    .append(" или пробегу ").append(mileageToFind).append(": ")
                    .append(String.join(" ", numbers)).append("\n")
                    .append("Уникальные модели в диапазоне цен от ").append(minPrice)
                    .append(" до ").append(maxPrice).append(": ").append(uniqueModelsCount).append(" шт.\n")
                    .append("Цвет самого дешевого автомобиля: ").append(cheapestColor).append("\n")
                    .append(String.format("Средняя стоимость %s: %.2f\n", modelToFind1, avgCost1))
                    .append(String.format("Средняя стоимость %s: %.2f\n", modelToFind2, avgCost2));

            FileHandler.writeResultsToFile("output.txt", results.toString());
            System.out.println("\nРезультаты сохранены в файл output.txt");

        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлом: " + e.getMessage());
        }
    }
}