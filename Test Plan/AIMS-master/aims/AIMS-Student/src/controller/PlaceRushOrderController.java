package controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
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
import entity.order.RushOrder;
import utils.Utils;
import views.screen.popup.PopupScreen;

/**
 * This class controls the flow of place rush  order usecase in our AIMS project
 *
 * @author khang
 * <p>
 * Create at: 12/1/2020
 * <p>
 * Project name: AIMS-Student
 * <p>
 * Teacher's name: Dr. Nguyen Thi Thu Trang
 * <p>
 * Class name: TT.CNTT ICT 02 K62
 * <p>
 * Helpers: Teaching assistants
 */

public class PlaceRushOrderController extends PlaceOrderController{



    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceRushOrderController.class.getName());

    /**
     * This method checks the avalibility of product when user click PlaceOrder button
     * @throws SQLException
     */
    public void placeRushOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }


    /**
     * This method creates the new rushOrder based on the Cart
     * @return rushOrder
     * @throws SQLException
     */
    public RushOrder createRushOrder() throws SQLException{
        RushOrder rushOrder = new RushOrder();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(),
                    cartMedia.getQuantity(),
                    cartMedia.getPrice());
            rushOrder.getlstOrderMedia().add(orderMedia);
        }
        return rushOrder;
    }

    /**
     * This method creates the new Invoice based on rush order
     * @param rushOrder
     * @return Invoice
     */
    public Invoice createInvoice(Order order,RushOrder rushOrder) {
        return new Invoice(rushOrder);
    }


    /**
     * The method validates the info
     * @param info customer info
     * @throws InterruptedException
     * @throws IOException
     */
    public String validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{
        if(!validateSupportedProvince(info.get("province"))) {
            return ("INVALID PROVINCE");
        }
        if(!validateSupportedItems()) {
            return ("INVALID ITEMS");
        }
        if(!validateReceiveDateTime(info.get("rushDeliveryTime"))) {
            return ("INVALID TIME");
        }
        return "ok";
    }

    /**
     * This method validates received datetime for rush order
     * @return true if valid
     */
    public boolean validateReceiveDateTime (String timeString) {
        try {
            LocalDateTime time = LocalDateTime.parse(timeString, Utils.formatter);
            if (time.compareTo(LocalDateTime.now()) <= 0) {
                return false;
            }
            if (time.getHour() > 18 || time.getHour() < 9) {
                return false;
            }
        } catch (NullPointerException | DateTimeParseException e) {
            return false;
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

    /**
     * Validate items
     * @return true if valid
     */
    public boolean validateSupportedItems(){
        try {
            if (Cart.getCart().getListRushMedia().size() == 0) {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * Calculates shipping fees
     * @param rushOrder rush order
     * @return shipping fees
     */
    public int calculateShippingFee(Order rushOrder) {
        int fees = (int) (((0.5 * 10) / 100) * rushOrder.getAmount());
        fees += 10000 * rushOrder.getlstOrderMedia().size();
        LOGGER.info("Order Amount: " + rushOrder.getAmount() + " -- Shipping Fees: " + fees);
        rushOrder.setShippingFees(fees);
        return fees;
    }


}
