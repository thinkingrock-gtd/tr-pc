/*
 * ThinkingRock, a project management tool for Personal Computers.
 * Copyright (C) 2006 Avente Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
