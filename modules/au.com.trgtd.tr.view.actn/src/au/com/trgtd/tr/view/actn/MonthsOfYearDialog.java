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
import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import org.openide.util.NbBundle;
import tr.model.action.PeriodYear;

/**
 * Months of the year selection dialog.
 * 
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public class MonthsOfYearDialog extends JDialog {

    /** Creates new form DaysOfMonthDialog */
    public MonthsOfYearDialog(Dialog parent, Component component) {
        super(parent, true);
        this.component = component;
        this.periodYear = new PeriodYear();
        initComponents();
        labels = new JLabel[]{
            labelJan, labelFeb, labelMar, labelApr, labelMay, labelJun,
            labelJul, labelAug, labelSep, labelOct, labelNov, labelDec,
        };
        for (int i = 0; i < labels.length; i++) {
            labels[i].setName(Integer.toString(i));
        }
        // default Ok button
        getRootPane().setDefaultButton(okButton);
        // Escape key to cancel
        ActionListener escapeListener = (ActionEvent evt) -> {
            cancel(evt);
        };
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(escapeListener, stroke,
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        okButton.requestFocusInWindow();
    }
    
    /** Sets the period week. */
    public void setPeriodYear(PeriodYear periodYear) {
        if (periodYear == null) {
            return;
        }
        this.periodYear = (PeriodYear) periodYear.clone();
        for (JLabel label : labels) {
            deselectLabel(label);
        }
        for (int month : periodYear.getSelectedMonths()) {
            selectMonth(month);
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

    private boolean isSelectedMonth(int monthIndex) {
        return periodYear.isSelected(monthIndex);
    }

    private void selectMonth(int monthIndex) {
        periodYear.select(monthIndex);
        selectLabel(labels[monthIndex]);
    }

    private void deselectMonth(int monthIndex) {
        periodYear.deselected(monthIndex);
        deselectLabel(labels[monthIndex]);
    }

    public List<Integer> getSelectedMonths() {
        if (periodYear == null) {
            return new Vector<>();
        }
        return periodYear.getSelectedMonths();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            cancelled = true;
        }
        super.setLocationRelativeTo(component);
        super.setVisible(visible);
    }

    public boolean cancelled() {
        return cancelled;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        daysPanel = new javax.swing.JPanel();
        labelJan = new javax.swing.JLabel();
        labelFeb = new javax.swing.JLabel();
        labelMar = new javax.swing.JLabel();
        labelApr = new javax.swing.JLabel();
        labelMay = new javax.swing.JLabel();
        labelJun = new javax.swing.JLabel();
        labelJul = new javax.swing.JLabel();
        labelAug = new javax.swing.JLabel();
        labelSep = new javax.swing.JLabel();
        labelOct = new javax.swing.JLabel();
        labelNov = new javax.swing.JLabel();
        labelDec = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(NbBundle.getMessage(MonthsOfYearDialog.class, "MonthsDialog.title")); // NOI18N

        daysPanel.setLayout(new java.awt.GridLayout(4, 3));

        labelJan.setBackground(DESELECTED_BG_COLOR);
        labelJan.setFont(labelJan.getFont().deriveFont(labelJan.getFont().getStyle() | java.awt.Font.BOLD));
        labelJan.setForeground(DESELECTED_FG_COLOR);
        labelJan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelJan.setText(NbBundle.getMessage(MonthsOfYearDialog.class, "MonthsDialog.jan")); // NOI18N
        labelJan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        labelJan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelJan.setOpaque(true);
        labelJan.setPreferredSize(new java.awt.Dimension(34, 34));
        labelJan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                monthMouseClicked(evt);
            }
        });
        daysPanel.add(labelJan);

        labelFeb.setBackground(DESELECTED_BG_COLOR);
        labelFeb.setFont(labelFeb.getFont().deriveFont(labelFeb.getFont().getStyle() | java.awt.Font.BOLD));
        labelFeb.setForeground(DESELECTED_FG_COLOR);
        labelFeb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelFeb.setText(NbBundle.getMessage(MonthsOfYearDialog.class, "MonthsDialog.feb")); // NOI18N
        labelFeb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        labelFeb.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelFeb.setOpaque(true);
        labelFeb.setPreferredSize(new java.awt.Dimension(34, 34));
        labelFeb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                monthMouseClicked(evt);
            }
        });
        daysPanel.add(labelFeb);

        labelMar.setBackground(DESELECTED_BG_COLOR);
        labelMar.setFont(labelMar.getFont().deriveFont(labelMar.getFont().getStyle() | java.awt.Font.BOLD));
        labelMar.setForeground(DESELECTED_FG_COLOR);
        labelMar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMar.setText(NbBundle.getMessage(MonthsOfYearDialog.class, "MonthsDialog.march")); // NOI18N
        labelMar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        labelMar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelMar.setOpaque(true);
        labelMar.setPreferredSize(new java.awt.Dimension(34, 34));
        labelMar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                monthMouseClicked(evt);
            }
        });
        daysPanel.add(labelMar);

        labelApr.setBackground(DESELECTED_BG_COLOR);
        labelApr.setFont(labelApr.getFont().deriveFont(labelApr.getFont().getStyle() | java.awt.Font.BOLD));
        labelApr.setForeground(DESELECTED_FG_COLOR);
        labelApr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelApr.setText(NbBundle.getMessage(MonthsOfYearDialog.class, "MonthsDialog.apr")); // NOI18N
        labelApr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        labelApr.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelApr.setOpaque(true);
        labelApr.setPreferredSize(new java.awt.Dimension(34, 34));
        labelApr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                monthMouseClicked(evt);
            }
        });
        daysPanel.add(labelApr);

        labelMay.setBackground(DESELECTED_BG_COLOR);
        labelMay.setFont(labelMay.getFont().deriveFont(labelMay.getFont().getStyle() | java.awt.Font.BOLD));
        labelMay.setForeground(DESELECTED_FG_COLOR);
        labelMay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMay.setText(NbBundle.getMessage(MonthsOfYearDialog.class, "MonthsDialog.may")); // NOI18N
        labelMay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        labelMay.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelMay.setOpaque(true);
        labelMay.setPreferredSize(new java.awt.Dimension(34, 34));
        labelMay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                monthMouseClicked(evt);
            }
        });
        daysPanel.add(labelMay);

        labelJun.setBackground(DESELECTED_BG_COLOR);
        labelJun.setFont(labelJun.getFont().deriveFont(labelJun.getFont().getStyle() | java.awt.Font.BOLD));
        labelJun.setForeground(DESELECTED_FG_COLOR);
        labelJun.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelJun.setText(NbBundle.getMessage(MonthsOfYearDialog.class, "MonthsDialog.jun")); // NOI18N
        labelJun.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        labelJun.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelJun.setOpaque(true);
        labelJun.setPreferredSize(new java.awt.Dimension(34, 34));
        labelJun.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                monthMouseClicked(evt);
            }
        });
        daysPanel.add(labelJun);

        labelJul.setBackground(DESELECTED_BG_COLOR);
        labelJul.setFont(labelJul.getFont().deriveFont(labelJul.getFont().getStyle() | java.awt.Font.BOLD));
        labelJul.setForeground(DESELECTED_FG_COLOR);
        labelJul.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelJul.setText(NbBundle.getMessage(MonthsOfYearDialog.class, "MonthsDialog.jul")); // NOI18N
        labelJul.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        labelJul.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelJul.setOpaque(true);
        labelJul.setPreferredSize(new java.awt.Dimension(34, 34));
        labelJul.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                monthMouseClicked(evt);
            }
        });
        daysPanel.add(labelJul);

        labelAug.setBackground(DESELECTED_BG_COLOR);
        labelAug.setFont(labelAug.getFont().deriveFont(labelAug.getFont().getStyle() | java.awt.Font.BOLD));
        labelAug.setForeground(DESELECTED_FG_COLOR);
        labelAug.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAug.setText(NbBundle.getMessage(MonthsOfYearDialog.class, "MonthsDialog.aug")); // NOI18N
        labelAug.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        labelAug.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelAug.setOpaque(true);
        labelAug.setPreferredSize(new java.awt.Dimension(34, 34));
        labelAug.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                monthMouseClicked(evt);
            }
        });
        daysPanel.add(labelAug);

        labelSep.setBackground(DESELECTED_BG_COLOR);
        labelSep.setFont(labelSep.getFont().deriveFont(labelSep.getFont().getStyle() | java.awt.Font.BOLD));
        labelSep.setForeground(DESELECTED_FG_COLOR);
        labelSep.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelSep.setText(NbBundle.getMessage(MonthsOfYearDialog.class, "MonthsDialog.sep")); // NOI18N
        labelSep.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        labelSep.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelSep.setOpaque(true);
        labelSep.setPreferredSize(new java.awt.Dimension(34, 34));
        labelSep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                monthMouseClicked(evt);
            }
        });
        daysPanel.add(labelSep);

        labelOct.setBackground(DESELECTED_BG_COLOR);
        labelOct.setFont(labelOct.getFont().deriveFont(labelOct.getFont().getStyle() | java.awt.Font.BOLD));
        labelOct.setForeground(DESELECTED_FG_COLOR);
        labelOct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelOct.setText(NbBundle.getMessage(MonthsOfYearDialog.class, "MonthsDialog.Oct")); // NOI18N
        labelOct.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        labelOct.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelOct.setOpaque(true);
        labelOct.setPreferredSize(new java.awt.Dimension(34, 34));
        labelOct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                monthMouseClicked(evt);
            }
        });
        daysPanel.add(labelOct);

        labelNov.setBackground(DESELECTED_BG_COLOR);
        labelNov.setFont(labelNov.getFont().deriveFont(labelNov.getFont().getStyle() | java.awt.Font.BOLD));
        labelNov.setForeground(DESELECTED_FG_COLOR);
        labelNov.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelNov.setText(NbBundle.getMessage(MonthsOfYearDialog.class, "MonthsDialog.nov")); // NOI18N
        labelNov.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        labelNov.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelNov.setOpaque(true);
        labelNov.setPreferredSize(new java.awt.Dimension(34, 34));
        labelNov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                monthMouseClicked(evt);
            }
        });
        daysPanel.add(labelNov);

        labelDec.setBackground(DESELECTED_BG_COLOR);
        labelDec.setFont(labelDec.getFont().deriveFont(labelDec.getFont().getStyle() | java.awt.Font.BOLD));
        labelDec.setForeground(DESELECTED_FG_COLOR);
        labelDec.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDec.setText(NbBundle.getMessage(MonthsOfYearDialog.class, "MonthsDialog.dec")); // NOI18N
        labelDec.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        labelDec.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelDec.setOpaque(true);
        labelDec.setPreferredSize(new java.awt.Dimension(34, 34));
        labelDec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                monthMouseClicked(evt);
            }
        });
        daysPanel.add(labelDec);

        getContentPane().add(daysPanel, java.awt.BorderLayout.CENTER);

        buttonsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        cancelButton.setText(NbBundle.getMessage(MonthsOfYearDialog.class, "Cancel")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel(evt);
            }
        });
        buttonsPanel.add(cancelButton);

        okButton.setText(NbBundle.getMessage(MonthsOfYearDialog.class, "Ok")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                done(evt);
            }
        });
        buttonsPanel.add(okButton);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void done(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_done
        cancelled = false;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_done

    private void cancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel
        cancelled = true;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_cancel

    private void monthMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_monthMouseClicked
        try {
            JLabel dayLabel = (JLabel) evt.getSource();
            int monthIndex = Integer.parseInt(dayLabel.getName());
            if (isSelectedMonth(monthIndex)) {
                deselectMonth(monthIndex);
            } else {
                selectMonth(monthIndex);
            }
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_monthMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel daysPanel;
    private javax.swing.JLabel labelApr;
    private javax.swing.JLabel labelAug;
    private javax.swing.JLabel labelDec;
    private javax.swing.JLabel labelFeb;
    private javax.swing.JLabel labelJan;
    private javax.swing.JLabel labelJul;
    private javax.swing.JLabel labelJun;
    private javax.swing.JLabel labelMar;
    private javax.swing.JLabel labelMay;
    private javax.swing.JLabel labelNov;
    private javax.swing.JLabel labelOct;
    private javax.swing.JLabel labelSep;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private static final Color SELECTED_BG_COLOR = UIManager.getDefaults().getColor("List.selectionBackground");
    private static final Color SELECTED_FG_COLOR = UIManager.getDefaults().getColor("List.selectionForeground");
    private static final Color DESELECTED_BG_COLOR = UIManager.getDefaults().getColor("List.background");
    private static final Color DESELECTED_FG_COLOR = UIManager.getDefaults().getColor("List.foreground");
    private final Component component;
    private final JLabel[] labels;
    private PeriodYear periodYear;
    private boolean cancelled;
}
