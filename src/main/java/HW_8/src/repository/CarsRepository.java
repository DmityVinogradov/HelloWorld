package HW_8.src.repository;
import HW_8.src.model.Car;

import java.util.List;

public interface CarsRepository {
    void addCar(Car car);
    List<Car> getAllCars();
    List<String> findNumbersByColorOrMileage(String colorToFind, long mileageToFind);
    long countUniqueModelsInPriceRange(double minPrice, double maxPrice);
    String findColorOfCheapestCar();
    double findAverageCostByModel(String modelToFind);
}