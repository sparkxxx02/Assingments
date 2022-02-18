package factorydesign.components;

import factorydesign.factory.Vehicle;

public class Car extends Vehicle {
    int wheel;

    public Car(int wheel) {
        this.wheel = wheel;
    }

    @Override
    public int getWheel() {
        return this.wheel;
    }
}
