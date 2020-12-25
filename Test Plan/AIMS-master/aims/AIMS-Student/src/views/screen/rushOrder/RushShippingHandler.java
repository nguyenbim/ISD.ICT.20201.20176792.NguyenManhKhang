package views.screen.rushOrder;

import common.exception.InvalidDeliveryInfoException;
import common.exception.MediaNotAvailableException;
import common.exception.PlaceOrderException;
import common.exception.ProcessInvoiceException;
import controller.BaseController;
import controller.PlaceOrderController;
import controller.PlaceRushOrderController;
import controller.ViewCartController;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import entity.order.RushOrder;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;

import views.screen.invoice.InvoiceScreenHandler;
import views.screen.popup.PopupScreen;
import views.screen.rushOrder.MediaRushScreenHandler;
import views.screen.rushOrder.RushInvoiceHandler;
import views.screen.shipping.ShippingScreenHandler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
/**
 * This class is used for handle delivery information for rush order option
 *
 * @author khang
 * <p>
 * Create at: 20/12/20
 * <p>
 * Project name: AIMS-Student
 * <p>
 * Teacher's name: Dr. Nguyen Thi Thu Trang
 * <p>
 * Class name: TT.CNTT ICT 02 K62
 * <p>
 * Helpers: Teaching assistants
 */
public class RushShippingHandler extends BaseScreenHandler {

    private static Logger LOGGER = Utils.getLogger(RushShippingHandler.class.getName());

    @FXML
    private VBox vboxItems;

    @FXML
    private Button rushConfirm;


    @FXML
    private TextField date;

    @FXML
    private TextField time;

    @FXML
    private Label error;

    @FXML
    private Label name;

    @FXML
    private Label address;

    @FXML
    private Label province;

    @FXML
    private Label phone;

    @FXML
    private Label instructions;

    private  Invoice invoice;
    private  Order order;


    public RushShippingHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
        super(stage, screenPath);
        this.invoice = invoice;
        this.order = this.invoice.getOrder();
        setInfo();
        displayInfo();

    }

    /**
     * Displays base delivery information
     */
    private void displayInfo() {
        name.setText((String) order.getDeliveryInfo().get("name"));
        address.setText((String) order.getDeliveryInfo().get("address"));
        province.setText((String) order.getDeliveryInfo().get("province"));
        phone.setText((String) order.getDeliveryInfo().get("phone"));
        instructions.setText((String) order.getDeliveryInfo().get("instructions"));
    }

    /**
     * Setups delivery information and transition to rush invoice screen
     * @param messages delivery info
     * @throws SQLException
     * @throws IOException
     */
    private void displayRushInvoice(HashMap messages) throws SQLException, IOException {
        List orderMedias = new ArrayList();
        Boolean validInfo = false;
        String deliveryFormMessage = "";
        String rushFormMessage = "";

        for (Object om : invoice.getOrder().getlstOrderMedia()) {
            if (!((OrderMedia) om).getMedia().isRushSupported()) {
                orderMedias.add(om);
            }
        }

        RushOrder rushOrder = getBController().createRushOrder();

        if (orderMedias.size() > 0) {
            order.setlstOrderMedia(orderMedias);
        } else {
            order = null;
        }

        try {
            deliveryFormMessage = getBController().processDeliveryInfo(messages);
            rushFormMessage = getBController().validateDeliveryInfo(messages);

            validInfo = deliveryFormMessage.equals("ok");
            validInfo = rushFormMessage.equals("ok");
        } catch (InvalidDeliveryInfoException | InterruptedException e) {
            validInfo = false;
            throw new InvalidDeliveryInfoException(e.getMessage());
        }


        if (validInfo) {
            // calculate shipping fees
            int shippingFees = getBController().calculateShippingFee(rushOrder);
            rushOrder.setShippingFees(shippingFees);
            rushOrder.setDeliveryInfo(messages);
            error.setVisible(false);
            try {
                Invoice invoice = getBController().createInvoice(order, rushOrder);
                BaseScreenHandler rushInvoiceHandler = new RushInvoiceHandler(this.stage, Configs.RUSH_INVOICE_PATH, invoice);
                rushInvoiceHandler.setPreviousScreen(this);
                rushInvoiceHandler.setHomeScreenHandler(homeScreenHandler);
                rushInvoiceHandler.setScreenTitle("Invoice Screen");
                rushInvoiceHandler.setBController(getBController());
                rushInvoiceHandler.show();
            } catch (MediaNotAvailableException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("SUBMIT FAILED");
            error.setVisible(true);
            if(!deliveryFormMessage.equals("ok")) {
                error.setText(deliveryFormMessage);
            } else {
                error.setText(rushFormMessage);
            }
            System.out.println(deliveryFormMessage);
            System.out.println(rushFormMessage);
        }
    }


    private void setInfo() {
        this.order.getlstOrderMedia().forEach(orderMedia -> {
            try {
                MediaRushScreenHandler mis = new MediaRushScreenHandler(Configs.INVOICE_MEDIA_SCREEN_PATH);
                mis.setOrderMedia((OrderMedia) orderMedia);
                if (((OrderMedia) orderMedia).getMedia().isRushSupported()) {
                    vboxItems.getChildren().add(mis.getContent());
                }
            } catch (IOException | SQLException e) {
                System.err.println("errors: " + e.getMessage());
                throw new ProcessInvoiceException(e.getMessage());
            }
        });

    }
//    public PlaceOrderController getBController() {
//        return (PlaceOrderController) super.getBController();
//    }

    public PlaceRushOrderController getBController() {
        return (PlaceRushOrderController) super.getBController();
    }

    /**
     * Handles event "click confirm rush order"
     * @param event - mouse clicked
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void submitRushDeliveryInfo(MouseEvent event) throws IOException, SQLException {
        // add info to messages
        HashMap messages = new HashMap<>();
        messages.put("name", name.getText());
        messages.put("phone", phone.getText());
        messages.put("address", address.getText());
        messages.put("instructions", instructions.getText());
        messages.put("province", province.getText());
        messages.put("rushDeliveryTime", date.getText() + " " + time.getText());

        displayRushInvoice(messages);
    }

}
