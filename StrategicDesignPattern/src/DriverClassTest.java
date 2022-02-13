import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DriverClassTest {

    @Test
    void test()
    {
        assertEquals("hello from Ujjwal", DriverClass.decide("Ujjwal"));
    }

}