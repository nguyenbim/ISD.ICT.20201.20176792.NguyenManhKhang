import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import controller.PlaceRushOrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidateReceiveDateTime{
    private PlaceRushOrderController placeRushOrderController;
    @BeforeEach
    void setUp() throws Exception {
        placeRushOrderController = new PlaceRushOrderController();
    }
    @ParameterizedTest
    @CsvSource({
            ", false",
            "1999-04-12 11:30,false",
            "2999-04-12 20:30,false",
            "2999-04-12 14:30,true",
            "abcd ,false"
    })

    public void test(String timeString, boolean expected) {
        boolean isValid = placeRushOrderController.validateReceiveDateTime(timeString);
        assertEquals(expected, isValid);
    }

}
