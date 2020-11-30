import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import controller.PlaceRushOrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidateSupportedProvince{
    private PlaceRushOrderController placeRushOrderController;
    @BeforeEach
    void setUp() throws Exception {
        placeRushOrderController = new PlaceRushOrderController();
    }
    @ParameterizedTest
    @CsvSource({
            "Ha Noi,true",
            "123,false",
            "hanoi,true",
            "Hà Nội,true",
            "Nghe An, false",
            ",false"
    })

    public void test(String province, boolean expected) {
        boolean isValid = placeRushOrderController.validateSupportedProvince(province);
        assertEquals(expected, isValid);
    }

}
