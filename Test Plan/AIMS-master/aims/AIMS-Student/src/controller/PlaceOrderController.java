package controller;

import java.io.IOException;
import java.sql.SQLException;
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
 * This class controls the flow of place order usecase in our AIMS project
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController{

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * This method checks the avalibility of product when user click PlaceOrder button
     * @throws SQLException
     */
    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     * @return Order
     * @throws SQLException
     */
    public Order createOrder() throws SQLException{
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), 
                                                   cartMedia.getQuantity(), 
                                                   cartMedia.getPrice());    
            order.getlstOrderMedia().add(orderMedia);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     * @param order chosen order
     * @return Invoice
     */
    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     * @param info delivery information
     * @throws InterruptedException
     * @throws IOException
     */
    public String processDeliveryInfo(HashMap info) throws InterruptedException, IOException{
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
       return validateDeliveryInfo(info);
    }
    
    /**
   * The method validates the info
   * @param info delivery info
   * @throws InterruptedException
   * @throws IOException
   */
    public String validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{
        if(!validateName(info.get("name"))) {
            return ("INVALID NAME");
        }
        if(!validatePhoneNumber(info.get("phone"))) {
            return ("INVALID PHONE NUMBER");
        }
        if(!validateProvince(info.get("province"))) {
            return ("SELECT PROVINCE");
        }
        if(!validateAddress(info.get("address"))) {
            return ("INVALID ADDRESS");
        }

        return "ok";
    }

    /**
     * Validate phone number
     * @param phoneNumber phone number
     * @return true if valid
     */
    public boolean validatePhoneNumber(String phoneNumber) {
    	// TODO: your work
        if(phoneNumber.length() != 10) return false;
        if(!phoneNumber.startsWith("0")) return false;
        try{
            Integer.parseInt(phoneNumber);
        } catch (NumberFormatException e){
            return false;
        }
    	return true;
    }

    /**
     * validate name
     * @param name owner name
     * @return true if valid
     */
    public boolean validateName(String name) {
    	// TODO: your work
        if(name == null) return false;
        else{
            for (char c : name.toCharArray()) {
                if (c <65) return false;
                if (c > 90 && c < 97) return false;
                if (c > 122) return false;
            }
        }
    	return true;
    }
    /**
     * validate address
     * @param address delivery address
     * @return true if valid
     */
    public boolean validateAddress(String address) {
    	// TODO: your work
        if(address==null) return false;
        else {
            for (char c : address.toCharArray()) {
                if(c!=32 && c<48) return false;
                if (c< 65 && c>57 ) return false;
                if (c > 90 && c < 97) return false;
                if (c > 122) return false;
            }
        }
    	return true;
    }
    /**
     * validate province
     * @param province delivery province
     * @return true if valid
     */
    public boolean validateProvince(String province) {
        try {
            if(province.isBlank()) {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
    

    /**
     * This method calculates the shipping fees of order
     * @param order chosen order
     * @return shippingFee
     */
    public int calculateShippingFee(Order order){
        Random rand = new Random();
        int fees = (int)( ( (rand.nextFloat()*10)/100 ) * order.getAmount() );
        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
        order.setShippingFees(fees);
        return fees;
    }
}
