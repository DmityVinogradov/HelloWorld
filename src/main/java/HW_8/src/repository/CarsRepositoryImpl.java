package HW_8.src.repository;

import HW_8.src.model.Car;
import java.util.*;
import java.util.stream.Collectors;

public class CarsRepositoryImpl implements CarsRepository {
    private List<Car> cars;

    public CarsRepositoryImpl() {
        this.cars = new ArrayList<>();
    }

    public CarsRepositoryImpl(List<Car> cars) {
        this.cars = new ArrayList<>(cars);
    }

    @Override
    public void addCar(Car car) {
        cars.add(car);
    }

    @Override
    public List<Car> getAllCars() {
        return new ArrayList<>(cars);
    }

    @Override
    public List<String> findNumbersByColorOrMileage(String colorToFind, long mileageToFind) {
        return cars.stream()
                .filter(car -> car.getColor().equalsIgnoreCase(colorToFind) || car.getMileage() == mileageToFind)
                .map(Car::getNumber)
                .collect(Collectors.toList());
    }

    @Override
    public long countUniqueModelsInPriceRange(double minPrice, double maxPrice) {
        return cars.stream()
                .filter(car -> car.getCost() >= minPrice && car.getCost() <= maxPrice)
                .map(Car::getModel)
                .distinct()
                .count();
    }

    @Override
    public String findColorOfCheapestCar() {
        return cars.stream()
                .min(Comparator.comparingDouble(Car::getCost))
                .map(Car::getColor)
                .orElse("Не найден");
    }

    @Override
    public double findAverageCostByModel(String modelToFind) {
        return cars.stream()
                .filter(car -> car.getModel().equalsIgnoreCase(modelToFind))
                .mapToDouble(Car::getCost)
                .average()
                .orElse(0.0);
    }

    // Дополнительные методы для удобства работы
    public void addAllCars(List<Car> carsList) {
        cars.addAll(carsList);
    }

    public void clear() {
        cars.clear();
    }

    public int size() {
        return cars.size();
    }

    public boolean isEmpty() {
        return cars.isEmpty();
    }

    public List<Car> findCarsByModel(String model) {
        return cars.stream()
                .filter(car -> car.getModel().equalsIgnoreCase(model))
                .collect(Collectors.toList());
    }

    public List<Car> findCarsByColor(String color) {
        return cars.stream()
                .filter(car -> car.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }
}