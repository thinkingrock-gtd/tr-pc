package au.com.trgtd.tr.view.ui;

import java.awt.Color;

public class ColoredStringImpl implements ColoredString {

    public final Color bgColor;
    public final Color fgColor;
    private String string;

    public ColoredStringImpl(String string, Color bgColor, Color fgColor) {
        this.string = string;
        this.bgColor = bgColor;
        this.fgColor = fgColor;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public Color getBGColor() {
        return bgColor;
    }

    public Color getFGColor() {
        return fgColor;
    }

}
