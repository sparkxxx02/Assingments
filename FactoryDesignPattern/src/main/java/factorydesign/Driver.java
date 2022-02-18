package factorydesign;

import factorydesign.factory.Vehicle;
import factorydesign.factory.VehicleFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Driver {
   static  Logger logger = LogManager.getLogger(Driver.class);

    public static void main(String[] args) {
        logger.info("Generating FactoryDesignPattern");

        Vehicle car = VehicleFactory.getInstance("car", 4);
        logger.debug(car);
        Vehicle bike = VehicleFactory.getInstance("bike", 2);
        logger.debug(bike);
    }
}
