package factorydesign.factory;

import factorydesign.components.Bike;
import factorydesign.components.Car;

import java.util.Objects;

public class VehicleFactory {
    public static Vehicle getInstance(String type, int wheel) {
        if(Objects.equals(type, "car")) {
            return new Car(wheel);
        } else if(Objects.equals(type, "bike")) {
            return new Bike(wheel);
        }

        return null;
    }
}