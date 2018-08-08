package au.com.trgtd.tr.export.data.model;

import java.awt.Color;

public final class TopicBean {
    
    private final int id;
    private final String title;
    private final String descr;
    private final Color bg;
    private final Color fg;

    public TopicBean(int id, String title, String descr, Color bg, Color fg) {
        this.id = id;
        this.title = title;
        this.descr = descr;
        this.bg = bg;
        this.fg = fg;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescr() {
        return descr;
    }

    public Color getBg() {
        return bg;
    }

    public Color getFg() {
        return fg;
    }

    public Integer getBgRGB() {
        return bg == null ? null : bg.getRGB();
    }

    public Integer getFgRGB() {
        return fg == null ? null : fg.getRGB();
    }
    
}
