package controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import views.screen.popup.PopupScreen;

/**
 * This class controls the flow of place rush  order usecase in our AIMS project
 * @author HUST
 * <p>
 * Create at: 30/11/20
 * <p>
 * Project name: AIMS-Student
 * <p>
 * Teacher's name: Dr. Nguyen Thi Thu Trang
 * <p>
 * Class name: TT.CNTT ICT 02 K62
 * <p>
 * Helpers: Teaching assistants
 */

public class PlaceRushOrderController extends BaseController{

    /**
     * The method validates the info
     * @param info Information for rush order
     * @throws InterruptedException
     * @throws IOException
     */
    public void validateRushDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException {

    }

    /**
     * This method validates received datetime for rush order
     * @param fromTime lower bounder
     * @param toTime upper bounder
     * @param date datetime
     * @return true if valid
     */
    public boolean validateReceiveDateTime (String fromTime, String toTime, String date) {
        String fromDateTime  = fromTime + " " + date;
        String toDateTime = toTime + " " + date;
        if(fromTime == null || toTime == null|| date == null) return false;
        else {
            try {
                DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                Date currentDateTime = new Date();
                Date fromDateTimeAfterConverted = dateTimeFormat.parse(fromDateTime);
                Date toDateTimeAfterConverted = dateTimeFormat.parse(toDateTime);
                if (fromDateTimeAfterConverted.after(toDateTimeAfterConverted) || currentDateTime.after(toDateTimeAfterConverted))
                    return false;
            } catch (ParseException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method check the province for rush order
     * @param province province
     * @return true if province is hanoi
     */
    public boolean validateSupportedProvince (String province) {
        if(province == null){}
        else {
            province = province.toLowerCase();

            if ((province.contains("hà nội") && province.length() == 6) || (province.contains("ha noi") && province.length() == 6) || (province.contains("hanoi") && province.length() == 5)) {
                return true;
            }
            return false;
        }
        return false;

    }
    public boolean validateSupportedItems(){
        return true;
    }


}
