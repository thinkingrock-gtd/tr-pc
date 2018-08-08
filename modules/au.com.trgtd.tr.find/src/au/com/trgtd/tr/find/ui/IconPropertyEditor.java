package au.com.trgtd.tr.find.ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyEditorSupport;
import javax.swing.ImageIcon;

public class IconPropertyEditor extends PropertyEditorSupport {

    /** Creates a new instance. */
    public IconPropertyEditor() {
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paintValue(Graphics g, Rectangle r) {    
        ImageIcon icon = (ImageIcon)getValue();
        if (icon != null) {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            int yOffset = r.height > h ? (int)(r.height - h) / 2 : 0;
            Graphics g2 = g.create(r.x, r.y, r.width, r.height);
            g2.drawImage(icon.getImage(), r.x, r.y + yOffset, w, h, null);
        }
    }



}
