package au.com.trgtd.tr.swing.date.field;

import au.com.trgtd.tr.prefs.dates.DatesPrefs;
import au.com.trgtd.tr.swing.date.chooser.DateChooser;
import au.com.trgtd.tr.swing.date.chooser.DateChooserDialog;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.MaskFormatter;
import org.openide.util.NbBundle;
import au.com.trgtd.tr.util.Utils;


public class DateField extends JComponent implements ActionListener, FocusListener {

    private static final Dimension SIZE = new Dimension(110, 23);
    
    private static final String FORMAT = NbBundle.getMessage(DateField.class, "format");
    
    private static final String[] TOOL_TIPS = {
        FORMAT + ": DD/MM/YYYY",
        FORMAT + ": MM/DD/YYYY",
        FORMAT + ": YYYY/MM/DD"
    };
    
    private static final char PLACEHOLDER = '_';
    private static final String[] MASKS = {
        "##/##/####",
        "##/##/####",
        "####/##/##"
    };
    
    private static DateFormat DISPLAY_FORMAT = getDisplayFormat();
    private static DateFormat INPUT_FORMAT = getInputFormat();
    
    private static Icon icon = new ImageIcon(DateField.class.getResource("/au/com/trgtd/tr/swing/date/field/Down16.gif"));
    
    private final JFormattedTextField ftf;
    private final JButton btn;
    
    private DateChooser dateChooser;
    
    private Date date;
    
    private static DateFormat getInputFormat() {
        DateFormat inputFormat = DatesPrefs.getDateFormat(DatesPrefs.DF_SHORT);
        inputFormat.setLenient(false);
        return inputFormat;
    }
    
    private static DateFormat getDisplayFormat() {
        DateFormat displayFormat = DatesPrefs.getDateFormat(DatesPrefs.DF_MEDIUM);
        displayFormat.setLenient(false);
        return displayFormat;
    }

    private static String getMask() {
        return MASKS[DatesPrefs.getDateOrder()];
    }
  
    private static String getToolTip() {
        return TOOL_TIPS[DatesPrefs.getDateOrder()];
    }
  

    public DateField() {
        this.ftf = new FTF(getMaskFormatter());
        this.btn = createButton();
        this.ftf.addPropertyChangeListener("value", new ValueDateGuard());
        this.ftf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    e.consume();
                    popupCalendar();
                }
            }
        });
        initialise();

        setPreferredSize(SIZE);
        setMinimumSize(SIZE);
        setMaximumSize(SIZE);
    }
    
    private static MaskFormatter getMaskFormatter() {
        try {
            MaskFormatter maskFormatter = new DateMaskFormatter(getMask());
            maskFormatter.setPlaceholderCharacter(PLACEHOLDER);
            return maskFormatter;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void initialise() {
        
        setBorder(BorderFactory.createEmptyBorder());
        
        btn.addActionListener(this);
        ftf.addActionListener(this);
        ftf.addFocusListener(this);
        ftf.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(ftf);
        this.add(Box.createHorizontalGlue());
        this.add(btn);
        
        DatesPrefs.getPrefs().addPreferenceChangeListener(new PreferenceChangeListener() {
            public void preferenceChange(PreferenceChangeEvent evt) {
                if (evt.getKey().equals(DatesPrefs.KEY_DATE_ORDER)) {
                    INPUT_FORMAT = getInputFormat();
                    DISPLAY_FORMAT = getDisplayFormat();
                }
            }
        });
    }
    
    private JButton createButton() {
        JButton button = new JButton();
        button.setIcon(icon);
        button.setFocusable(false);
        int width = 18;        
        int minHeight = 23;        
        int maxHeight = 27;
        int height = getPreferredSize().height;
        height = height < minHeight ? minHeight: height; 
        height = height > maxHeight ? maxHeight: height;             
        button.setPreferredSize(new Dimension(width, height));
        button.setMinimumSize(new Dimension(width, minHeight));
        button.setMaximumSize(new Dimension(width, maxHeight));                
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        return button;
    }
    
    /**
     * return the date as a <code>Date</code> object
     *
     * @return date return the date currently in the display field
     *         or null if there is no date displayed
     */
    public Date getDate() {
        return date;
    }
    
    public final JFormattedTextField getDateField() {
        return ftf;
    }
    
    /**
     * receives <code>ActionEvent</code>s and process them.
     *
     * @param ae ActionEvent process event fired by the button
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(btn)) {
            popupCalendar();
        } else if (ae.getSource().equals(ftf)) {
            setDate(getTextDate());
        }
    }
    
    /**
     * Process focus gained events
     *
     * @param fe FocusEvent focus event
     */
    public void focusGained(FocusEvent fe) {
    }
    
    /**
     * Process focus lost events
     * @param fe FocusEvent focus event
     */
    public void focusLost(FocusEvent fe) {
        if (fe.getSource().equals(ftf) && !fe.isTemporary()) {
            setDate(getTextDate());
        }
    }
    
    public final JButton getButton() {
        return btn;
    }
    
    /**
     * Validate date in dateField.
     * return Date if the date was valid or null if it was invalid.
     */
    protected final Date getTextDate() {
        try {
            return parseDate(ftf.getText());
        } catch (ParseException e) {
            return null;
        }
    }
    
    private static Date parseDate(String text) throws ParseException {
        try {
            return INPUT_FORMAT.parse(text);
        } catch (ParseException pe) {
            return DISPLAY_FORMAT.parse(text);
        }
    }
    
    /**
     * Get value from formatted text field.
     */
    protected Date getValueDate() {
        try {
            return DISPLAY_FORMAT.parse((String) ftf.getValue());
        } catch (ParseException e) {
            return null;
        }
    }
    
    private void popupCalendar() {
        if (dateChooser == null) {
            Window parent = SwingUtilities.windowForComponent(this);
            if (parent instanceof Frame) {
                dateChooser = new DateChooser((Frame)parent);
            } else if (parent instanceof Dialog) {
                dateChooser = new DateChooser((Dialog)parent);
            } else {
                dateChooser = new DateChooser(new Frame());
            }
        }
        DateChooserDialog dialog = dateChooser.getDialog();
        Date newDate = dialog.select(date, this);
        if (dialog.cancelled()) {
            return;
        } else {
            setDate(newDate);
            ftf.transferFocus();
        }
    }
    
    /**
     * setValue the date and display it in the text field
     * @param date setValue the date
     */
    public void setDate(Date date) {
        // @since 1.0.6
        if (Utils.equal(this.date, date)) {
            return;
        }
        // end change        
        setInternalDate(date);
        setValueDate(date);
        setToolTipText();
    }
    
    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        this.btn.setEnabled(b);
        this.ftf.setEnabled(b);
    }
    
    protected final void setInternalDate(Date newDate) {
        if (Utils.equal(this.date, newDate)) {
            return;
        }        
        Date oldDate = this.date;        
        this.date = newDate;        
        firePropertyChange("value", oldDate, newDate);
    }
    
    private void setToolTipText() {
        if (ftf == null) {
            return;
        }        
        if (date == null) {
            ftf.setToolTipText(getToolTip());
        } else {
            ftf.setToolTipText(DatesPrefs.formatLong(date) + " (" + getToolTip() + ")");
        }
    }
    
    /**
     * Set value into formatted text field.
     * @param date The date
     */
    protected void setValueDate(Date date) {
        ftf.setValue(date == null ? null : DISPLAY_FORMAT.format(date));
    }
    
    private static class DateMaskFormatter extends MaskFormatter {
        public DateMaskFormatter(String mask) throws ParseException {
            super(mask);
        }
        @Override
        public Object stringToValue(String value) throws ParseException {
            try {
                return super.stringToValue(value);
            } catch (ParseException e) {
                try {
                    return DISPLAY_FORMAT.format(parseDate(value));
                } catch (ParseException notcheckedException) {
                    throw e;
                }
            }
        }
    }
    
    /**
     * Listener that directly change date field to make it available since
     * changes is done before focus events
     */
    private final class ValueDateGuard implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            setInternalDate(getTextDate());
        }
    }

    public Insets getMargin() {
        return this.ftf.getMargin();
    }

    private static class FTF extends JFormattedTextField {
        public FTF(MaskFormatter f) {
            super(f);
        }
        @Override // to not beep
        protected void invalidEdit() {
        }
    }
    
}
