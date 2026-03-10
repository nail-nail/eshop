package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import java.util.List;

public interface CarService {
    public Car create(Car car);
    List<Car> findAll();
    Car findById(String carId);
    void update(String carId, Car car);
    void delete(String carId);
}
