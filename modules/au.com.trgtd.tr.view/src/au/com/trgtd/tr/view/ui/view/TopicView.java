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
package au.com.trgtd.tr.view.ui.view;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import tr.model.topic.Topic;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.Key;

public class TopicView extends JPanel implements Observer {

    private final static Color TRANSPARENT = new Color(0, 0, 0, 0);
    private final static Border LABEL_BORDER = new EmptyBorder(1, 4, 1, 4);
    private final static int PANEL_ARC = 10;
    private final static Insets PANEL_INSETS = new Insets(0, 0, 0, 0);
    private final static Map<Key, Object> RENDER_HINTS = new HashMap<>();
    static {RENDER_HINTS.put(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);}

    private final PanelBorder panelBorder = new PanelBorder();
    private final JLabel label = new JLabel();
    private Topic topic;

    public TopicView() {
        super(new BorderLayout());
        setBorder(panelBorder);
        setBackground(TRANSPARENT);
        label.setBorder(LABEL_BORDER);
        add(label, BorderLayout.CENTER);
        setTopic(Topic.getDefault());
    }

    @Override
    public int getBaseline(int width, int height) {
        return label.getBaseline(width, height);
    }

    @Override
    public BaselineResizeBehavior getBaselineResizeBehavior() {
        return label.getBaselineResizeBehavior();
    }

    public void setTopic(Topic topic) {
        if (this.topic != null) {
            this.topic.removeObserver(this);
        }
        this.topic = (topic == null ? Topic.getDefault() : topic);
        this.topic.addObserver(this);
        initialise();
    }

    private void initialise() {
        panelBorder.setColor(topic.equals(Topic.getDefault()) ? TRANSPARENT : topic.getBackground());
        label.setForeground(topic.getForeground());
        label.setText(topic.getName());
        revalidate();
        repaint();
    }

    public void update(Observable obs, Object arg) {
        initialise();
    }


    private final static class PanelBorder implements Border {

        private Color colorFill = TRANSPARENT;
        private Color colorLine = TRANSPARENT;

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.addRenderingHints(RENDER_HINTS);
            g2d.setColor(colorFill);
            g2d.fillRoundRect(x, y, w - 1, h - 1, PANEL_ARC, PANEL_ARC);
            g2d.setColor(colorLine);
            g2d.drawRoundRect(x, y, w - 1, h - 1, PANEL_ARC, PANEL_ARC);
        }

        public Insets getBorderInsets(Component c) {
            return PANEL_INSETS;
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void setColor(Color color) {
            if (color.getAlpha() == 0) {
                colorFill = TRANSPARENT;
                colorLine = TRANSPARENT;
            } else {
                colorFill = color;
                colorLine = color.darker();
            }
        }
    }
    
}
