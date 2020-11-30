package entity.order;

import java.util.ArrayList;
import java.util.List;

/**
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

public class RushOrder extends Order {
    protected String rushDeliveryTime;
    public RushOrder(){
        this.lstOrderMedia = new ArrayList<>();
    }
    public RushOrder(List lstOrderMedia){
        this.lstOrderMedia = lstOrderMedia;
    }
    public String getRushDeliveryTime(){
        return  rushDeliveryTime;
    }

    public void setRushDeliveryTime(String rushDeliveryTime) {
        this.rushDeliveryTime = rushDeliveryTime;
    }
    public void addOrderMedia(OrderMedia om){
        if(om.getMedia().isRushSupported()){
            this.lstOrderMedia.add(om);
        }
    }

}
