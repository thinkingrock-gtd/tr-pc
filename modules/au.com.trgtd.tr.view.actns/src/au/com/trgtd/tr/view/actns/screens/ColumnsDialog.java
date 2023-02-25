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
package au.com.trgtd.tr.view.actns.screens;

import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;

/**
 * Review actions screen columns dialog.
 *
 * @author Jeremy Moore
 */
public class ColumnsDialog extends JDialog {
    
    /** Creates new form ColumnsDialog */
    public ColumnsDialog(ActionsScreen screen) {
        super(WindowManager.getDefault().getMainWindow(), true);
        this.screen = screen;
        initComponents();
    }    

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        table = getTable();
        bottomPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(getText("ColumnsDialog.title")); // NOI18N
        setModal(true);

        scrollPane.setFocusable(false);
        scrollPane.setOpaque(false);
        scrollPane.setPreferredSize(new java.awt.Dimension(300, 500));

        table.setFocusable(false);
        table.setGridColor(java.awt.SystemColor.control);
        table.setRowSelectionAllowed(false);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        scrollPane.setViewportView(table);

        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

        bottomPanel.setFocusable(false);
        bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 20, 5));

        closeButton.setText(getText("ColumnsDialog.close")); // NOI18N
        closeButton.setFocusCycleRoot(true);
        closeButton.setSelected(true);
        getRootPane().setDefaultButton(closeButton);
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeActionPerformed(evt);
            }
        });
        bottomPanel.add(closeButton);

        getContentPane().add(bottomPanel, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeActionPerformed
    
    private JTable getTable() {
        JTable t = new JTable(getTableModel());
        
        // left justify headings
        JTableHeader tableHeader = t.getTableHeader();
        TableCellRenderer tcr = tableHeader.getDefaultRenderer();
        if (tcr instanceof DefaultTableCellRenderer renderer) {
            renderer.setHorizontalAlignment(SwingConstants.LEFT);
        }
        
        // set renderers and column sizes
        for (int column = 0; column < getTableModel().getColumnCount(); column++) {
            TableColumn tc = t.getColumnModel().getColumn(column);
            if (tableModel.getTableCellRenderer(column) != null) {
                tc.setCellRenderer(tableModel.getTableCellRenderer(column));
            }
            if (tableModel.getMaximumWidth(column) > -1) {
                tc.setMaxWidth(tableModel.getMaximumWidth(column));
            }
            if (tableModel.getMinimumWidth(column) > -1) {
                tc.setMinWidth(tableModel.getMinimumWidth(column));
            }
            if (tableModel.getPreferredWidth(column) > -1) {
                tc.setPreferredWidth(tableModel.getPreferredWidth(column));
            }
        }
        
        t.setRowHeight(24);
        
        return t;
    }
    
    private TableModel getTableModel() {
        if (tableModel == null) {
            tableModel = new ColumnsTableModel(screen);
        }
        return tableModel;
    }
    
    private String getText(String key) {
        return NbBundle.getMessage(getClass(), key);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
    
    private final ActionsScreen screen;
    private ColumnsTableModel tableModel;
    
}
