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
import tr.model.action.PeriodMonth;

/**
 * Days of the month selection dialog.
 * 
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public class DaysOfMonthDialog extends JDialog {

    /** Creates new form DaysOfMonthDialog */
    public DaysOfMonthDialog(Dialog parent, Component component) {
        super(parent, true);
        this.component = component;
        this.periodMonth = new PeriodMonth();
        initComponents();
        labels = new JLabel[]{
            null,
            label01, label02, label03, label04, label05, label06, label07,
            label08, label09, label10, label11, label12, label13, label14,
            label15, label16, label17, label18, label19, label20, label21,
            label22, label23, label24, label25, label26, label27, label28,
            label29, label30, label31,
        };
        for (int i = 1; i < labels.length; i++) {
            labels[i].setName(Integer.toString(i));
        }
        // default Ok button
        getRootPane().setDefaultButton(doneButton);
        // Escape key to cancel
        ActionListener escapeListener = (ActionEvent evt) -> {
            cancel(evt);
        };
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(escapeListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        doneButton.requestFocusInWindow();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        daysPanel = new javax.swing.JPanel();
        label01 = new javax.swing.JLabel();
        label02 = new javax.swing.JLabel();
        label03 = new javax.swing.JLabel();
        label04 = new javax.swing.JLabel();
        label05 = new javax.swing.JLabel();
        label06 = new javax.swing.JLabel();
        label07 = new javax.swing.JLabel();
        label08 = new javax.swing.JLabel();
        label09 = new javax.swing.JLabel();
        label10 = new javax.swing.JLabel();
        label11 = new javax.swing.JLabel();
        label12 = new javax.swing.JLabel();
        label13 = new javax.swing.JLabel();
        label14 = new javax.swing.JLabel();
        label15 = new javax.swing.JLabel();
        label16 = new javax.swing.JLabel();
        label17 = new javax.swing.JLabel();
        label18 = new javax.swing.JLabel();
        label19 = new javax.swing.JLabel();
        label20 = new javax.swing.JLabel();
        label21 = new javax.swing.JLabel();
        label22 = new javax.swing.JLabel();
        label23 = new javax.swing.JLabel();
        label24 = new javax.swing.JLabel();
        label25 = new javax.swing.JLabel();
        label26 = new javax.swing.JLabel();
        label27 = new javax.swing.JLabel();
        label28 = new javax.swing.JLabel();
        label29 = new javax.swing.JLabel();
        label30 = new javax.swing.JLabel();
        label31 = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        doneButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.title")); // NOI18N

        daysPanel.setLayout(new java.awt.GridLayout(5, 7));

        label01.setBackground(DESELECTED_BG_COLOR);
        label01.setFont(label01.getFont().deriveFont(label01.getFont().getStyle() | java.awt.Font.BOLD));
        label01.setForeground(DESELECTED_FG_COLOR);
        label01.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label01.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label01.text")); // NOI18N
        label01.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label01.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label01.setOpaque(true);
        label01.setPreferredSize(new java.awt.Dimension(34, 34));
        label01.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label01);

        label02.setBackground(DESELECTED_BG_COLOR);
        label02.setFont(label02.getFont().deriveFont(label02.getFont().getStyle() | java.awt.Font.BOLD));
        label02.setForeground(DESELECTED_FG_COLOR);
        label02.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label02.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label02.text")); // NOI18N
        label02.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label02.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label02.setOpaque(true);
        label02.setPreferredSize(new java.awt.Dimension(34, 34));
        label02.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label02);

        label03.setBackground(DESELECTED_BG_COLOR);
        label03.setFont(label03.getFont().deriveFont(label03.getFont().getStyle() | java.awt.Font.BOLD));
        label03.setForeground(DESELECTED_FG_COLOR);
        label03.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label03.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label03.text")); // NOI18N
        label03.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label03.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label03.setOpaque(true);
        label03.setPreferredSize(new java.awt.Dimension(34, 34));
        label03.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label03);

        label04.setBackground(DESELECTED_BG_COLOR);
        label04.setFont(label04.getFont().deriveFont(label04.getFont().getStyle() | java.awt.Font.BOLD));
        label04.setForeground(DESELECTED_FG_COLOR);
        label04.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label04.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label04.text")); // NOI18N
        label04.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label04.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label04.setOpaque(true);
        label04.setPreferredSize(new java.awt.Dimension(34, 34));
        label04.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label04);

        label05.setBackground(DESELECTED_BG_COLOR);
        label05.setFont(label05.getFont().deriveFont(label05.getFont().getStyle() | java.awt.Font.BOLD));
        label05.setForeground(DESELECTED_FG_COLOR);
        label05.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label05.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label05.text")); // NOI18N
        label05.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label05.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label05.setOpaque(true);
        label05.setPreferredSize(new java.awt.Dimension(34, 34));
        label05.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label05);

        label06.setBackground(DESELECTED_BG_COLOR);
        label06.setFont(label06.getFont().deriveFont(label06.getFont().getStyle() | java.awt.Font.BOLD));
        label06.setForeground(DESELECTED_FG_COLOR);
        label06.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label06.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label06.text")); // NOI18N
        label06.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label06.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label06.setOpaque(true);
        label06.setPreferredSize(new java.awt.Dimension(34, 34));
        label06.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label06);

        label07.setBackground(DESELECTED_BG_COLOR);
        label07.setFont(label07.getFont().deriveFont(label07.getFont().getStyle() | java.awt.Font.BOLD));
        label07.setForeground(DESELECTED_FG_COLOR);
        label07.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label07.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label07.text")); // NOI18N
        label07.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label07.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label07.setOpaque(true);
        label07.setPreferredSize(new java.awt.Dimension(34, 34));
        label07.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label07);

        label08.setBackground(DESELECTED_BG_COLOR);
        label08.setFont(label08.getFont().deriveFont(label08.getFont().getStyle() | java.awt.Font.BOLD));
        label08.setForeground(DESELECTED_FG_COLOR);
        label08.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label08.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label08.text")); // NOI18N
        label08.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label08.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label08.setOpaque(true);
        label08.setPreferredSize(new java.awt.Dimension(34, 34));
        label08.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label08);

        label09.setBackground(DESELECTED_BG_COLOR);
        label09.setFont(label09.getFont().deriveFont(label09.getFont().getStyle() | java.awt.Font.BOLD));
        label09.setForeground(DESELECTED_FG_COLOR);
        label09.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label09.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label09.text")); // NOI18N
        label09.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label09.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label09.setOpaque(true);
        label09.setPreferredSize(new java.awt.Dimension(34, 34));
        label09.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label09);

        label10.setBackground(DESELECTED_BG_COLOR);
        label10.setFont(label10.getFont().deriveFont(label10.getFont().getStyle() | java.awt.Font.BOLD));
        label10.setForeground(DESELECTED_FG_COLOR);
        label10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label10.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label10.text")); // NOI18N
        label10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label10.setOpaque(true);
        label10.setPreferredSize(new java.awt.Dimension(34, 34));
        label10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label10);

        label11.setBackground(DESELECTED_BG_COLOR);
        label11.setFont(label11.getFont().deriveFont(label11.getFont().getStyle() | java.awt.Font.BOLD));
        label11.setForeground(DESELECTED_FG_COLOR);
        label11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label11.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label11.text")); // NOI18N
        label11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label11.setOpaque(true);
        label11.setPreferredSize(new java.awt.Dimension(34, 34));
        label11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label11);

        label12.setBackground(DESELECTED_BG_COLOR);
        label12.setFont(label12.getFont().deriveFont(label12.getFont().getStyle() | java.awt.Font.BOLD));
        label12.setForeground(DESELECTED_FG_COLOR);
        label12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label12.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label12.text")); // NOI18N
        label12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label12.setOpaque(true);
        label12.setPreferredSize(new java.awt.Dimension(34, 34));
        label12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label12);

        label13.setBackground(DESELECTED_BG_COLOR);
        label13.setFont(label13.getFont().deriveFont(label13.getFont().getStyle() | java.awt.Font.BOLD));
        label13.setForeground(DESELECTED_FG_COLOR);
        label13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label13.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label13.text")); // NOI18N
        label13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label13.setOpaque(true);
        label13.setPreferredSize(new java.awt.Dimension(34, 34));
        label13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label13);

        label14.setBackground(DESELECTED_BG_COLOR);
        label14.setFont(label14.getFont().deriveFont(label14.getFont().getStyle() | java.awt.Font.BOLD));
        label14.setForeground(DESELECTED_FG_COLOR);
        label14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label14.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label14.text")); // NOI18N
        label14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label14.setOpaque(true);
        label14.setPreferredSize(new java.awt.Dimension(34, 34));
        label14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label14);

        label15.setBackground(DESELECTED_BG_COLOR);
        label15.setFont(label15.getFont().deriveFont(label15.getFont().getStyle() | java.awt.Font.BOLD));
        label15.setForeground(DESELECTED_FG_COLOR);
        label15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label15.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label15.text")); // NOI18N
        label15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label15.setOpaque(true);
        label15.setPreferredSize(new java.awt.Dimension(34, 34));
        label15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label15);

        label16.setBackground(DESELECTED_BG_COLOR);
        label16.setFont(label16.getFont().deriveFont(label16.getFont().getStyle() | java.awt.Font.BOLD));
        label16.setForeground(DESELECTED_FG_COLOR);
        label16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label16.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label16.text")); // NOI18N
        label16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label16.setOpaque(true);
        label16.setPreferredSize(new java.awt.Dimension(34, 34));
        label16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label16);

        label17.setBackground(DESELECTED_BG_COLOR);
        label17.setFont(label17.getFont().deriveFont(label17.getFont().getStyle() | java.awt.Font.BOLD));
        label17.setForeground(DESELECTED_FG_COLOR);
        label17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label17.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label17.text")); // NOI18N
        label17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label17.setOpaque(true);
        label17.setPreferredSize(new java.awt.Dimension(34, 34));
        label17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label17);

        label18.setBackground(DESELECTED_BG_COLOR);
        label18.setFont(label18.getFont().deriveFont(label18.getFont().getStyle() | java.awt.Font.BOLD));
        label18.setForeground(DESELECTED_FG_COLOR);
        label18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label18.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label18.text")); // NOI18N
        label18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label18.setOpaque(true);
        label18.setPreferredSize(new java.awt.Dimension(34, 34));
        label18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label18);

        label19.setBackground(DESELECTED_BG_COLOR);
        label19.setFont(label19.getFont().deriveFont(label19.getFont().getStyle() | java.awt.Font.BOLD));
        label19.setForeground(DESELECTED_FG_COLOR);
        label19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label19.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label19.text")); // NOI18N
        label19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label19.setOpaque(true);
        label19.setPreferredSize(new java.awt.Dimension(34, 34));
        label19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label19);

        label20.setBackground(DESELECTED_BG_COLOR);
        label20.setFont(label20.getFont().deriveFont(label20.getFont().getStyle() | java.awt.Font.BOLD));
        label20.setForeground(DESELECTED_FG_COLOR);
        label20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label20.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label20.text")); // NOI18N
        label20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label20.setOpaque(true);
        label20.setPreferredSize(new java.awt.Dimension(34, 34));
        label20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label20);

        label21.setBackground(DESELECTED_BG_COLOR);
        label21.setFont(label21.getFont().deriveFont(label21.getFont().getStyle() | java.awt.Font.BOLD));
        label21.setForeground(DESELECTED_FG_COLOR);
        label21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label21.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label21.text")); // NOI18N
        label21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label21.setOpaque(true);
        label21.setPreferredSize(new java.awt.Dimension(34, 34));
        label21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label21);

        label22.setBackground(DESELECTED_BG_COLOR);
        label22.setFont(label22.getFont().deriveFont(label22.getFont().getStyle() | java.awt.Font.BOLD));
        label22.setForeground(DESELECTED_FG_COLOR);
        label22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label22.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label22.text")); // NOI18N
        label22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label22.setOpaque(true);
        label22.setPreferredSize(new java.awt.Dimension(34, 34));
        label22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label22);

        label23.setBackground(DESELECTED_BG_COLOR);
        label23.setFont(label23.getFont().deriveFont(label23.getFont().getStyle() | java.awt.Font.BOLD));
        label23.setForeground(DESELECTED_FG_COLOR);
        label23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label23.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label23.text")); // NOI18N
        label23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label23.setOpaque(true);
        label23.setPreferredSize(new java.awt.Dimension(34, 34));
        label23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label23);

        label24.setBackground(DESELECTED_BG_COLOR);
        label24.setFont(label24.getFont().deriveFont(label24.getFont().getStyle() | java.awt.Font.BOLD));
        label24.setForeground(DESELECTED_FG_COLOR);
        label24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label24.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label24.text")); // NOI18N
        label24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label24.setOpaque(true);
        label24.setPreferredSize(new java.awt.Dimension(34, 34));
        label24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label24);

        label25.setBackground(DESELECTED_BG_COLOR);
        label25.setFont(label25.getFont().deriveFont(label25.getFont().getStyle() | java.awt.Font.BOLD));
        label25.setForeground(DESELECTED_FG_COLOR);
        label25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label25.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label25.text")); // NOI18N
        label25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label25.setOpaque(true);
        label25.setPreferredSize(new java.awt.Dimension(34, 34));
        label25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label25);

        label26.setBackground(DESELECTED_BG_COLOR);
        label26.setFont(label26.getFont().deriveFont(label26.getFont().getStyle() | java.awt.Font.BOLD));
        label26.setForeground(DESELECTED_FG_COLOR);
        label26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label26.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label26.text")); // NOI18N
        label26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label26.setOpaque(true);
        label26.setPreferredSize(new java.awt.Dimension(34, 34));
        label26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label26);

        label27.setBackground(DESELECTED_BG_COLOR);
        label27.setFont(label27.getFont().deriveFont(label27.getFont().getStyle() | java.awt.Font.BOLD));
        label27.setForeground(DESELECTED_FG_COLOR);
        label27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label27.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label27.text")); // NOI18N
        label27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label27.setOpaque(true);
        label27.setPreferredSize(new java.awt.Dimension(34, 34));
        label27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label27);

        label28.setBackground(DESELECTED_BG_COLOR);
        label28.setFont(label28.getFont().deriveFont(label28.getFont().getStyle() | java.awt.Font.BOLD));
        label28.setForeground(DESELECTED_FG_COLOR);
        label28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label28.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label28.text")); // NOI18N
        label28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label28.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label28.setOpaque(true);
        label28.setPreferredSize(new java.awt.Dimension(34, 34));
        label28.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label28);

        label29.setBackground(DESELECTED_BG_COLOR);
        label29.setFont(label29.getFont().deriveFont(label29.getFont().getStyle() | java.awt.Font.BOLD));
        label29.setForeground(DESELECTED_FG_COLOR);
        label29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label29.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label29.text")); // NOI18N
        label29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label29.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label29.setOpaque(true);
        label29.setPreferredSize(new java.awt.Dimension(34, 34));
        label29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label29);

        label30.setBackground(DESELECTED_BG_COLOR);
        label30.setFont(label30.getFont().deriveFont(label30.getFont().getStyle() | java.awt.Font.BOLD));
        label30.setForeground(DESELECTED_FG_COLOR);
        label30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label30.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label30.text")); // NOI18N
        label30.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label30.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label30.setOpaque(true);
        label30.setPreferredSize(new java.awt.Dimension(34, 34));
        label30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label30);

        label31.setBackground(DESELECTED_BG_COLOR);
        label31.setFont(label31.getFont().deriveFont(label31.getFont().getStyle() | java.awt.Font.BOLD));
        label31.setForeground(DESELECTED_FG_COLOR);
        label31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label31.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "DaysOfMonthDialog.label31.text")); // NOI18N
        label31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        label31.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label31.setOpaque(true);
        label31.setPreferredSize(new java.awt.Dimension(34, 34));
        label31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        daysPanel.add(label31);

        getContentPane().add(daysPanel, java.awt.BorderLayout.CENTER);

        buttonsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        cancelButton.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "Cancel")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel(evt);
            }
        });
        buttonsPanel.add(cancelButton);

        doneButton.setText(NbBundle.getMessage(DaysOfMonthDialog.class, "Ok")); // NOI18N
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                done(evt);
            }
        });
        buttonsPanel.add(doneButton);

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

    private void dayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dayMouseClicked
        try {
            JLabel dayLabel = (JLabel) evt.getSource();
            int dayNumber = Integer.parseInt(dayLabel.getName());
            if (isSelectedDay(dayNumber)) {
                deselectDay(dayNumber);
            } else {
                selectDay(dayNumber);
            }
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_dayMouseClicked

    /** Sets the period week. */
    public void setPeriodMonth(PeriodMonth periodMonth) {
        if (periodMonth == null) {
            return;
        }
        this.periodMonth = (PeriodMonth) periodMonth.clone();
        for (JLabel label : labels) {
            deselectLabel(label);
        }
        for (int day : periodMonth.getSelectedDays()) {
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
        return periodMonth.isSelected(dayNumber);
    }

    private void selectDay(int dayNumber) {
        periodMonth.select(dayNumber);
        selectLabel(labels[dayNumber]);
    }

    private void deselectDay(int dayNumber) {
        periodMonth.deselected(dayNumber);
        deselectLabel(labels[dayNumber]);
    }

    public List<Integer> getSelectedDays() {
        if (periodMonth == null) {
            return new Vector<>();
        }
        return periodMonth.getSelectedDays();
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel daysPanel;
    private javax.swing.JButton doneButton;
    private javax.swing.JLabel label01;
    private javax.swing.JLabel label02;
    private javax.swing.JLabel label03;
    private javax.swing.JLabel label04;
    private javax.swing.JLabel label05;
    private javax.swing.JLabel label06;
    private javax.swing.JLabel label07;
    private javax.swing.JLabel label08;
    private javax.swing.JLabel label09;
    private javax.swing.JLabel label10;
    private javax.swing.JLabel label11;
    private javax.swing.JLabel label12;
    private javax.swing.JLabel label13;
    private javax.swing.JLabel label14;
    private javax.swing.JLabel label15;
    private javax.swing.JLabel label16;
    private javax.swing.JLabel label17;
    private javax.swing.JLabel label18;
    private javax.swing.JLabel label19;
    private javax.swing.JLabel label20;
    private javax.swing.JLabel label21;
    private javax.swing.JLabel label22;
    private javax.swing.JLabel label23;
    private javax.swing.JLabel label24;
    private javax.swing.JLabel label25;
    private javax.swing.JLabel label26;
    private javax.swing.JLabel label27;
    private javax.swing.JLabel label28;
    private javax.swing.JLabel label29;
    private javax.swing.JLabel label30;
    private javax.swing.JLabel label31;
    // End of variables declaration//GEN-END:variables
    private static final Color SELECTED_BG_COLOR = UIManager.getDefaults().getColor("List.selectionBackground");
    private static final Color SELECTED_FG_COLOR = UIManager.getDefaults().getColor("List.selectionForeground");
    private static final Color DESELECTED_BG_COLOR = UIManager.getDefaults().getColor("List.background");
    private static final Color DESELECTED_FG_COLOR = UIManager.getDefaults().getColor("List.foreground");
    private final Component component;
    private final JLabel[] labels;
    private PeriodMonth periodMonth;
    private boolean cancelled;
}
