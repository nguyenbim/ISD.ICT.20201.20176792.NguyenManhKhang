package views.screen.rushOrder;

import common.exception.ProcessInvoiceException;
import controller.PaymentController;
import controller.PlaceRushOrderController;
import entity.invoice.Invoice;
import entity.order.OrderMedia;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;
import views.screen.payment.PaymentScreenHandler;
import views.screen.rushOrder.MediaRushScreenHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;


/**
 * This class is used for handle invoice for rush order option
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

public class RushInvoiceHandler extends BaseScreenHandler {

    private static Logger LOGGER = Utils.getLogger(InvoiceScreenHandler.class.getName());

    @FXML
    private Label pageTitle;

    @FXML
    private Label name;

    @FXML
    private Label phone;

    @FXML
    private Label province;

    @FXML
    private Label address;

    @FXML
    private Label instructions;

    @FXML
    private Label subtotal;

    @FXML
    private Label shippingFees;

    @FXML
    private Label total;

    @FXML
    private Label shippingTimeLabel;

    @FXML
    private Label shippingTime;

    @FXML
    private VBox vboxItems;

    @FXML
    private VBox vboxItems2;


    private Invoice invoice;

    /**
     * Constructor
     * @param stage stage
     * @param screenPath screenPath
     * @param invoice chosen invoice
     * @throws IOException
     */
    public RushInvoiceHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
        super(stage, screenPath);
        this.invoice = invoice;
        setInvoiceInfo();
    }

    private void setInvoiceInfo() {
        setInvoiceInfoDetails(invoice.getRushOrder().getDeliveryInfo());
        setNormalOrderList();
        setRushOrderList();
    }

    /**
     * Adds delivery information to display
     * @param deliveryInfo delivery information
     */
    private void setInvoiceInfoDetails(HashMap<String, String> deliveryInfo) {
        name.setText(deliveryInfo.get("name"));
        province.setText(deliveryInfo.get("province"));
        instructions.setText(deliveryInfo.get("instructions"));
        address.setText(deliveryInfo.get("address"));
        int amount = 0;
        shippingTimeLabel.setVisible(true);
        shippingTime.setVisible(true);
        shippingTime.setText(deliveryInfo.get("rushDeliveryTime"));
        subtotal.setText(Utils.getCurrencyFormat(invoice.getRushOrder().getAmount()));
        shippingFees.setText(Utils.getCurrencyFormat(invoice.getRushOrder().getShippingFees()));
        amount = invoice.getRushOrder().getAmount() + invoice.getRushOrder().getShippingFees();
        total.setText(Utils.getCurrencyFormat(amount));

        invoice.setAmount(amount);
    }

    private void setNormalOrderList() {
        try {
            invoice.getOrder().getlstOrderMedia().forEach(orderMedia -> {
                try {
                    MediaRushScreenHandler mis = new MediaRushScreenHandler(Configs.INVOICE_MEDIA_SCREEN_PATH);
                    mis.setOrderMedia((OrderMedia) orderMedia);
                    vboxItems.getChildren().add(mis.getContent());

                } catch (IOException | SQLException e) {
                    System.err.println("errors: " + e.getMessage());
                    throw new ProcessInvoiceException(e.getMessage());
                }
            });
        } catch (NullPointerException e) {
            System.out.println("Every Items are rush supported");
        }

    }

    private void setRushOrderList() {
        invoice.getRushOrder().getlstOrderMedia().forEach(orderMedia -> {
            try {
                MediaRushScreenHandler mis = new MediaRushScreenHandler(Configs.INVOICE_MEDIA_SCREEN_PATH);
                mis.setOrderMedia((OrderMedia) orderMedia);
                vboxItems2.getChildren().add(mis.getContent());
            } catch (IOException | SQLException e) {
                System.err.println("errors: " + e.getMessage());
                throw new ProcessInvoiceException(e.getMessage());
            }

        });
    }

    @Override
    public PlaceRushOrderController getBController() {
        return (PlaceRushOrderController) super.getBController();
    }

    /**
     * Handles event click button "confirm Invoice"
     * @param event mouse clicked
     * @throws IOException
     */
    @FXML
    void confirmInvoice(MouseEvent event) throws IOException {
        PaymentScreenHandler paymentScreen = new PaymentScreenHandler(this.stage, Configs.PAYMENT_SCREEN_PATH, invoice);
        paymentScreen.setBController(new PaymentController());
        paymentScreen.setPreviousScreen(this);
        paymentScreen.setHomeScreenHandler(homeScreenHandler);
        paymentScreen.setScreenTitle("Payment Screen");
        paymentScreen.show();
        LOGGER.info("Confirmed invoice");
    }

}
