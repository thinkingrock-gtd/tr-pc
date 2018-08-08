package au.com.trgtd.tr.cal.utils;

import java.awt.Font;
import javax.swing.UIManager;

/**
 * Fonts.
 * 
 * @author jeremy moore
 */
public interface Fonts {

    public final Font NORMAL = UIManager.getFont("Label.font");
    
//    public final Font EVENT_TIME = NORMAL.deriveFont(9.0f);
//    public final Font EVENT_TEXT = NORMAL.deriveFont(9.0f);
    public final Font EVENT_TIME = NORMAL.deriveFont(10.0f);
    public final Font EVENT_TEXT = NORMAL.deriveFont(10.0f);
    
    public final Font DAY_TITLE  = NORMAL.deriveFont(10.0f).deriveFont(Font.BOLD);
    public final Font DAY_NUMBER = NORMAL.deriveFont(12.0f);
    
}
