package HW_8.src;

import java.util.*;
import java.util.stream.Collectors;

public class CarRepository {
    private List<Car> cars;

    public CarRepository() {
        this.cars = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public List<Car> getAllCars() {
        return new ArrayList<>(cars);
    }

    // 1) Номера автомобилей по цвету или пробегу
    public List<String> findNumbersByColorOrMileage(String colorToFind, long mileageToFind) {
        return cars.stream()
                .filter(car -> car.getColor().equalsIgnoreCase(colorToFind) || car.getMileage() == mileageToFind)
                .map(Car::getNumber)
                .collect(Collectors.toList());
    }

    // 2) Количество уникальных моделей в ценовом диапазоне
    public long countUniqueModelsInPriceRange(double minPrice, double maxPrice) {
        return cars.stream()
                .filter(car -> car.getCost() >= minPrice && car.getCost() <= maxPrice)
                .map(Car::getModel)
                .distinct()
                .count();
    }

    // 3) Цвет автомобиля с минимальной стоимостью
    public String findColorOfCheapestCar() {
        return cars.stream()
                .min(Comparator.comparingDouble(Car::getCost))
                .map(Car::getColor)
                .orElse("Не найден");
    }

    // 4) Средняя стоимость искомой модели
    public double findAverageCostByModel(String modelToFind) {
        return cars.stream()
                .filter(car -> car.getModel().equalsIgnoreCase(modelToFind))
                .mapToDouble(Car::getCost)
                .average()
                .orElse(0.0);
    }
}