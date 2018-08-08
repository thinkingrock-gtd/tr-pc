package au.com.trgtd.tr.view.ui;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * WidgetFactory - see Abstract Factory Pattern.
 *
 * @author Jeremy Moore
 */
public interface UIFactory {


    public JButton createButton();

    public JPanel createPanel();


    
}
