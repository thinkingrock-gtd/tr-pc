package au.com.trgtd.tr.view.ui;

import java.awt.Color;

public interface ColoredString extends Colored {

    public void setString(String str);
    public String getString();
    public Color getBGColor();
    public Color getFGColor();

}
