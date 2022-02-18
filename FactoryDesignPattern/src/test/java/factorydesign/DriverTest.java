package factorydesign;

import factorydesign.components.Bike;
import factorydesign.components.Car;
import factorydesign.factory.VehicleFactory;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class DriverTest {
    @Test
    void test()
    {
        Car car=new Car(4);
        Bike bike=new Bike(2);

        assertEquals(Objects.requireNonNull(VehicleFactory.getInstance("car", 4)).toString(),car.toString());
        assertEquals(Objects.requireNonNull(VehicleFactory.getInstance("bike", 2)).toString(),bike.toString());

    }

}