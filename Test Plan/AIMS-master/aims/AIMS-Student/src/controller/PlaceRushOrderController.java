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
    public Invoice createInvoice(RushOrder rushOrder) {
        return new Invoice(rushOrder);
    }
    /**
     * This method validates received datetime for rush order
     * @param fromTime lower bounder
     * @param toTime upper bounder
     * @param date datetime
     * @return true if valid
     */
    /**
     * This method takes responsibility for processing the shipping info from user
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException{
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }

    /**
     * The method validates the info
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{

    }

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
