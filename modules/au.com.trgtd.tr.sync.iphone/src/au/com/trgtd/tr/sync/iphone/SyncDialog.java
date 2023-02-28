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
package au.com.trgtd.tr.sync.iphone;

import au.com.trgtd.tr.swing.TRComboBox;
import au.com.trgtd.tr.swing.TRLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

/**
 * Custom dialog for iPhone syncing.
 */
public class SyncDialog extends JDialog {

    private static final Logger LOG = Logger.getLogger(SyncDialog.class.getName());

    private static String getText(String key) {
        return NbBundle.getMessage(SyncDialog.class, key);
    }

    private static String getText(String key, String p1, String p2) {
        return NbBundle.getMessage(SyncDialog.class, key, p1, p2);
    }
    private JPanel panel;
    private JPanel firstInputPanel;
    private JPanel firstOptnsPanel;
    private JButton firstNextButton;
    private JButton firstCancelButton;
    private JLabel addrLabel;
    private JComboBox<NetAddr> addrCombo;
    private JLabel portLabel;
    private JFormattedTextField portField;
    private JPanel infoPanel;
    private JTextArea textArea;
    private JProgressBar progress;
    private JPanel optsPanel;
    private JButton cancelConnectButton;
    private JButton cancelSyncingButton;
    private JButton finishButton;
    private PropertyChangeListener stateListener;
    private PropertyChangeListener progressListener;

    /**
     * Constructs a new instance.
     */
    public SyncDialog(Frame frame) {
        super(frame, getText("title"));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        construct();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        stateListener = (PropertyChangeEvent evt) -> {
            updateState((SyncState.State)evt.getNewValue());
        };

        SyncManager.getDefault().addPropertyChangeListener(SyncState.PROP_STATE, stateListener);

        progressListener = (PropertyChangeEvent evt) -> {
            updateProgress((Integer)evt.getNewValue());
        };
        SyncManager.getDefault().addPropertyChangeListener(SyncProgress.PROP_PROGRESS, progressListener);

        LOG.info("Added listeners");
    }

    @Override
    public void removeNotify() {
        SyncManager.getDefault().removePropertyChangeListener(SyncState.PROP_STATE, stateListener);
        stateListener = null;

        SyncManager.getDefault().removePropertyChangeListener(SyncProgress.PROP_PROGRESS, progressListener);
        progressListener = null;

        LOG.info("Removed listeners");

        super.removeNotify();
    }

    private static class NetAddr {

        public final NetworkInterface networkInterface;
        public final InetAddress inetAddress;
        public final String ip;
        public final String dn;

        public NetAddr(NetworkInterface networkInterface, InetAddress inetAddress) {
            this.networkInterface = networkInterface;
            this.inetAddress = inetAddress;
            this.ip = inetAddress.getHostAddress();
            this.dn = networkInterface.getDisplayName();
        }

        @Override
        public String toString() {
            String ip = inetAddress.getHostAddress();
            while (ip.length() < 15) {
                ip += " ";
            }
            String dn = networkInterface.getDisplayName();
            if (null == dn) {
                dn = "";
            }
            if (dn.length() > 56) {
                dn = dn.substring(0, 56) + " ...";
            }
            return ip + " " + dn;
        }
    }

    private List<NetAddr> getNetAddresses() {
        try {
            List<NetAddr> list = new Vector<>();
            for (NetworkInterface netint : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress inetaddr : Collections.list(netint.getInetAddresses())) {
                    String address = inetaddr.getHostAddress();
                    if (address.startsWith("127.0.0.1")) {
                        continue;
                    }
                    if (address.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                        list.add(new NetAddr(netint, inetaddr));
                    }
                }
            }
            return list;
        } catch (SocketException ex) {
            return Collections.emptyList();
        }
    }

    private void startSync() {
        NetAddr netaddr = (NetAddr) addrCombo.getSelectedItem();
        if (netaddr == null) {
            return;
        }
        panel.removeAll();
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(optsPanel, BorderLayout.SOUTH);
        panel.revalidate();
        panel.repaint();

        SyncManager.getDefault().startSync(netaddr.inetAddress);

        SyncPrefs.setLastAddr(netaddr.ip);
        SyncPrefs.setLastName(netaddr.dn);
    }

    private int getPortFieldValue() {
        Object value = portField.getValue();
        if (value instanceof Integer i) {
            return i;
        }
        if (value instanceof Long l) {
            return l.intValue();
        }
        return 0;
    }

    private void construct() {
        firstCancelButton = new JButton(getText("cancel"));
        firstCancelButton.addActionListener((ActionEvent e) -> {
            setVisible(false);
            dispose();
        });
        firstNextButton = new JButton(getText("next"));
        firstNextButton.setEnabled(false);
        firstNextButton.addActionListener((ActionEvent e) -> {
            SyncPrefs.setPort(getPortFieldValue());
            startSync();
        });
        FlowLayout firstFlow = new FlowLayout();
        firstFlow.setAlignment(FlowLayout.TRAILING);
        firstOptnsPanel = new JPanel(firstFlow);
        firstOptnsPanel.add(firstNextButton);
        firstOptnsPanel.add(firstCancelButton);


        List<NetAddr> netAddrs = getNetAddresses();
        addrLabel = new TRLabel(getText("choose.network.address"));
        addrCombo = new TRComboBox<>(netAddrs.toArray(NetAddr[]::new));
        addrCombo.setFont(new Font("Monospaced", Font.PLAIN, 14));
        addrCombo.addActionListener((ActionEvent e) -> {
            firstNextButton.setEnabled(addrCombo.getSelectedIndex() > -1);
        });

        String lastAddr = SyncPrefs.getLastAddr();
        String lastName = SyncPrefs.getLastName();
        for (NetAddr netAddr : netAddrs) {
            if (lastAddr.equals(netAddr.ip) || lastName.equals(netAddr.dn)) {
                addrCombo.setSelectedItem(netAddr);
                break;
            }
        }

        portLabel = new TRLabel(getText("port"));
        NumberFormat portFormat = NumberFormat.getIntegerInstance();
        portFormat.setGroupingUsed(false);
        portField = new JFormattedTextField(portFormat);
        portField.setValue(SyncPrefs.getPort());
        portField.setColumns(6);
        portField.addPropertyChangeListener("value", (PropertyChangeEvent evt) -> {
            int port = getPortFieldValue();
            if (port < 1) {
                Toolkit.getDefaultToolkit().beep();
                portField.setValue(SyncPrefs.getPort());
            }
        });

        firstInputPanel = new JPanel(new MigLayout("", "12[grow]2", "18[]3[]6[]3[]3"));
        firstInputPanel.add(addrLabel, "align left, wrap");
        firstInputPanel.add(addrCombo, "align left, wrap");
        firstInputPanel.add(portLabel, "align left, wrap");
        firstInputPanel.add(portField, "align left, wrap");

        panel = new JPanel(new BorderLayout());
        panel.add(firstInputPanel, BorderLayout.CENTER);
        panel.add(firstOptnsPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        firstNextButton.setEnabled(addrCombo.getSelectedIndex() > -1);

        ActionListener cancelConnectListener = (ActionEvent e) -> {
            SyncManager.getDefault().cancelConnect();
            setVisible(false);
            dispose();
        };
        ActionListener cancelSyncingListener = (ActionEvent e) -> {
            SyncManager.getDefault().cancelSync();
            setVisible(false);
            dispose();
        };
        ActionListener finishListener = (ActionEvent e) -> {
            setVisible(false);
            dispose();
        };
        cancelConnectButton = new JButton(getText("cancel"));
        cancelConnectButton.addActionListener(cancelConnectListener);
        cancelSyncingButton = new JButton(getText("cancel"));
        cancelSyncingButton.addActionListener(cancelSyncingListener);
        finishButton = new JButton(getText("close"));
        finishButton.addActionListener(finishListener);


        FlowLayout flow = new FlowLayout();
        flow.setAlignment(FlowLayout.TRAILING);
        optsPanel = new JPanel(flow);
        optsPanel.add(cancelConnectButton);

        textArea = new JTextArea();
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setAutoscrolls(true);
        progress = new JProgressBar(0, 100);

        infoPanel = new JPanel(new MigLayout("", "12[grow]12", "18[grow]2[]2"));
        infoPanel.add(textArea, "growx, growy, wrap");
        infoPanel.add(progress, "growx, wrap");

        getRootPane().setDefaultButton(firstNextButton);

        setPreferredSize(new Dimension(680, 300));
        setResizable(true);
        setLocationByPlatform(true);
        setAlwaysOnTop(true);
        pack();

    }

    private String getConnectText() {
        try {
            NetAddr netaddr = (NetAddr) addrCombo.getSelectedItem();
            String ipAddr = (netaddr == null ? "Unknown" : netaddr.inetAddress.getHostAddress());
            String portNr = portField.getText();
            return getText("waiting") + "\n\n" + getText("address", ipAddr, portNr) + "\n\n" + getText("tapsync");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    private String getSyncingText() {
        return getText("syncing");
    }

    private String getFinishedText() {
        return getText("finished");
    }

    private void updateState(SyncState.State state) {
        switch (state) {
            case None:
            case Connecting:
                textArea.setText(getConnectText());
                optsPanel.removeAll();
                optsPanel.add(cancelConnectButton);
                break;
            case Syncing:
                textArea.setText(getSyncingText());
                optsPanel.removeAll();
                optsPanel.add(cancelSyncingButton);
                break;
            case Finished:
                textArea.setText(getFinishedText());
                optsPanel.removeAll();
                optsPanel.add(finishButton);
                break;
            case Aborted:
                setVisible(false);
                dispose();
        }
    }

    private void updateProgress(int percent) {
        LOG.log(Level.INFO, "Progress update: {0}", percent);
        if (percent < 0 || percent > 100) {
            progress.setIndeterminate(true);
            progress.setValue(0);
        } else {
            progress.setIndeterminate(false);
            progress.setValue(percent);
        }
    }
}
