package au.com.trgtd.tr.swing.mig;

import net.miginfocom.layout.PlatformDefaults;

public class MigUtils {

    public static Boolean isOkCancelOrder;
    
    public static boolean isOkCancelOrder() {
        if (isOkCancelOrder == null) {
            String order = PlatformDefaults.getButtonOrder();
            int okayIndex = order.indexOf("O");
            int cancIndex = order.indexOf("C");
            isOkCancelOrder = okayIndex < cancIndex;
        }
        return isOkCancelOrder;
    }

}
