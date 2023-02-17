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
package au.com.trgtd.tr.view.actn;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.openide.util.NbBundle;
import tr.model.action.PeriodWeek;
import au.com.trgtd.tr.prefs.dates.DatesPrefs;

/**
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public class PeriodWeeklyPanel extends JPanel {

    private final JLabel[] dayLabels = new JLabel[8];
    private PeriodWeek periodWeek;

    /** Creates new form DaysOfWeekPanel */
    public PeriodWeeklyPanel() {
        this.periodWeek = new PeriodWeek();
        initComponents();
        initOther();
    }

    private void initOther() {
        lblSun.setName(Integer.toString(Calendar.SUNDAY));
        lblMon.setName(Integer.toString(Calendar.MONDAY));
        lblTue.setName(Integer.toString(Calendar.TUESDAY));
        lblWed.setName(Integer.toString(Calendar.WEDNESDAY));
        lblThu.setName(Integer.toString(Calendar.THURSDAY));
        lblFri.setName(Integer.toString(Calendar.FRIDAY));
        lblSat.setName(Integer.toString(Calendar.SATURDAY));
        dayLabels[Calendar.SUNDAY] = lblSun;
        dayLabels[Calendar.MONDAY] = lblMon;
        dayLabels[Calendar.TUESDAY] = lblTue;
        dayLabels[Calendar.WEDNESDAY] = lblWed;
        dayLabels[Calendar.THURSDAY] = lblThu;
        dayLabels[Calendar.FRIDAY] = lblFri;
        dayLabels[Calendar.SATURDAY] = lblSat;
        if (DatesPrefs.getFirstDayOfWeek() == DatesPrefs.MONDAY) {
            remove(lblSun); // remove sunday as first
            add(lblSun);    // add sunday as last
        }
    }

    /** Sets the period week. */
    public void setPeriodWeek(PeriodWeek periodWeek) {
        this.periodWeek = periodWeek;
        deselectLabel(dayLabels[Calendar.SUNDAY]);
        deselectLabel(dayLabels[Calendar.MONDAY]);
        deselectLabel(dayLabels[Calendar.TUESDAY]);
        deselectLabel(dayLabels[Calendar.WEDNESDAY]);
        deselectLabel(dayLabels[Calendar.THURSDAY]);
        deselectLabel(dayLabels[Calendar.FRIDAY]);
        deselectLabel(dayLabels[Calendar.SATURDAY]);
        for (int day : periodWeek.getSelectedDays()) {
            selectDay(day);
        }
    }

    private void selectLabel(JLabel label) {
        if (label == null) {
            return;
        }
        label.setBackground(SELECTED_BG_COLOR);
        label.setForeground(SELECTED_FG_COLOR);
    }

    private void deselectLabel(JLabel label) {
        if (label == null) {
            return;
        }        
        label.setBackground(DESELECTED_BG_COLOR);
        label.setForeground(DESELECTED_FG_COLOR);
    }

    private boolean isSelectedDay(int dayNumber) {
        return periodWeek.isSelected(dayNumber);
    }

    private void selectDay(int dayNumber) {
        periodWeek.select(dayNumber);
        selectLabel(dayLabels[dayNumber]);
    }

    private void deselectDay(int dayNumber) {
        periodWeek.deselected(dayNumber);
        deselectLabel(dayLabels[dayNumber]);
    }

    /**
     * Receives notification of the start date so that a default week day can
     * be set if necessary (i.e. if there is no weekday already set).
     * @startDate The start date
     */
    public void notifyStartDate(Date startDate) {
        if (startDate == null) {
            return;
        }
        if (!periodWeek.getSelectedDays().isEmpty()) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        selectDay(calendar.get(Calendar.DAY_OF_WEEK));
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        lblMon.setEnabled(enabled);
        lblTue.setEnabled(enabled);
        lblWed.setEnabled(enabled);
        lblThu.setEnabled(enabled);
        lblFri.setEnabled(enabled);
        lblSat.setEnabled(enabled);
        lblSun.setEnabled(enabled);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblSun = new javax.swing.JLabel();
        lblMon = new javax.swing.JLabel();
        lblTue = new javax.swing.JLabel();
        lblWed = new javax.swing.JLabel();
        lblThu = new javax.swing.JLabel();
        lblFri = new javax.swing.JLabel();
        lblSat = new javax.swing.JLabel();

        setLayout(new java.awt.GridLayout(1, 0));

        lblSun.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSun.setText(NbBundle.getMessage(PeriodWeeklyPanel.class, "sunday")); // NOI18N
        lblSun.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblSun.setMaximumSize(new java.awt.Dimension(50, 23));
        lblSun.setMinimumSize(new java.awt.Dimension(50, 23));
        lblSun.setOpaque(true);
        lblSun.setPreferredSize(new java.awt.Dimension(50, 23));
        lblSun.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PeriodWeeklyPanel.this.mouseClicked(evt);
            }
        });
        add(lblSun);

        lblMon.setForeground(DESELECTED_FG_COLOR);
        lblMon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMon.setText(NbBundle.getMessage(PeriodWeeklyPanel.class, "monday")); // NOI18N
        lblMon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblMon.setMaximumSize(new java.awt.Dimension(50, 23));
        lblMon.setMinimumSize(new java.awt.Dimension(50, 23));
        lblMon.setOpaque(true);
        lblMon.setPreferredSize(new java.awt.Dimension(50, 23));
        lblMon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PeriodWeeklyPanel.this.mouseClicked(evt);
            }
        });
        add(lblMon);

        lblTue.setForeground(DESELECTED_FG_COLOR);
        lblTue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTue.setText(NbBundle.getMessage(PeriodWeeklyPanel.class, "tuesday")); // NOI18N
        lblTue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblTue.setMaximumSize(new java.awt.Dimension(50, 23));
        lblTue.setMinimumSize(new java.awt.Dimension(50, 23));
        lblTue.setOpaque(true);
        lblTue.setPreferredSize(new java.awt.Dimension(50, 23));
        lblTue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PeriodWeeklyPanel.this.mouseClicked(evt);
            }
        });
        add(lblTue);

        lblWed.setForeground(DESELECTED_FG_COLOR);
        lblWed.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWed.setText(NbBundle.getMessage(PeriodWeeklyPanel.class, "wednesday")); // NOI18N
        lblWed.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblWed.setMaximumSize(new java.awt.Dimension(50, 23));
        lblWed.setMinimumSize(new java.awt.Dimension(50, 23));
        lblWed.setOpaque(true);
        lblWed.setPreferredSize(new java.awt.Dimension(50, 23));
        lblWed.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PeriodWeeklyPanel.this.mouseClicked(evt);
            }
        });
        add(lblWed);

        lblThu.setForeground(DESELECTED_FG_COLOR);
        lblThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblThu.setText(NbBundle.getMessage(PeriodWeeklyPanel.class, "thursday")); // NOI18N
        lblThu.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblThu.setMaximumSize(new java.awt.Dimension(50, 23));
        lblThu.setMinimumSize(new java.awt.Dimension(50, 23));
        lblThu.setOpaque(true);
        lblThu.setPreferredSize(new java.awt.Dimension(50, 23));
        lblThu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PeriodWeeklyPanel.this.mouseClicked(evt);
            }
        });
        add(lblThu);

        lblFri.setForeground(DESELECTED_FG_COLOR);
        lblFri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFri.setText(NbBundle.getMessage(PeriodWeeklyPanel.class, "friday")); // NOI18N
        lblFri.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblFri.setMaximumSize(new java.awt.Dimension(50, 23));
        lblFri.setMinimumSize(new java.awt.Dimension(50, 23));
        lblFri.setOpaque(true);
        lblFri.setPreferredSize(new java.awt.Dimension(50, 23));
        lblFri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PeriodWeeklyPanel.this.mouseClicked(evt);
            }
        });
        add(lblFri);

        lblSat.setForeground(DESELECTED_FG_COLOR);
        lblSat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSat.setText(NbBundle.getMessage(PeriodWeeklyPanel.class, "saturday")); // NOI18N
        lblSat.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblSat.setMaximumSize(new java.awt.Dimension(50, 23));
        lblSat.setMinimumSize(new java.awt.Dimension(50, 23));
        lblSat.setOpaque(true);
        lblSat.setPreferredSize(new java.awt.Dimension(50, 23));
        lblSat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PeriodWeeklyPanel.this.mouseClicked(evt);
            }
        });
        add(lblSat);
    }// </editor-fold>//GEN-END:initComponents

    private void mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClicked
        try {
            JLabel dayLabel = (JLabel) evt.getSource();
            if (dayLabel.isEnabled()) {
                int dayNumber = Integer.parseInt(dayLabel.getName());
                if (isSelectedDay(dayNumber)) {
                    deselectDay(dayNumber);
                } else {
                    selectDay(dayNumber);
                }
            }
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_mouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblFri;
    private javax.swing.JLabel lblMon;
    private javax.swing.JLabel lblSat;
    private javax.swing.JLabel lblSun;
    private javax.swing.JLabel lblThu;
    private javax.swing.JLabel lblTue;
    private javax.swing.JLabel lblWed;
    // End of variables declaration//GEN-END:variables
    private static final Color SELECTED_BG_COLOR = UIManager.getDefaults().getColor("List.selectionBackground");
    private static final Color SELECTED_FG_COLOR = UIManager.getDefaults().getColor("List.selectionForeground");
//    private static final Color DESELECTED_BG_COLOR = UIManager.getDefaults().getColor("List.background");
//    private static final Color DESELECTED_FG_COLOR = UIManager.getDefaults().getColor("List.foreground");
    private static final Color DESELECTED_BG_COLOR = UIManager.getDefaults().getColor("Label.background");
    private static final Color DESELECTED_FG_COLOR = UIManager.getDefaults().getColor("Label.foreground");
}
