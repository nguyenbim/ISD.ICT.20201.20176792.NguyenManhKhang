import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import controller.PlaceRushOrderController;
import controller.PlaceRushOrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

class ValidateReceiveDateTime{
    private PlaceRushOrderController placeRushOrderController;
    @BeforeEach
    void setUp() throws Exception {
        placeRushOrderController = new PlaceRushOrderController();
    }
    @ParameterizedTest
    @CsvSource({
            "07:00,10:00,30/11/2020,true",
            "8:00,7:00,30/11/2020,false",
            "7:00,12:00,26/11/2020,true",
            "123,10:00,30/11/2020,false",
            "7:00,10:00,abc,false",
            "7:00,,,false",
            ",,,false"
    })
    @Test
    public void test(String fromTime, String toTime, String date, boolean expected) {
        boolean isValid = placeRushOrderController.validateReceiveDateTime(fromTime,toTime,date);
        assertEquals(expected, isValid);
    }

}
