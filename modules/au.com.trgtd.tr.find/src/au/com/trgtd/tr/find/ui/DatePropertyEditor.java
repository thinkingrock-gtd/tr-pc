package au.com.trgtd.tr.find.ui;

import au.com.trgtd.tr.appl.Constants;
import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class DatePropertyEditor extends PropertyEditorSupport {

    private static final DateFormat DATE_FORMAT = Constants.DATE_FORMAT_FIXED;
    
    @Override
    public String getAsText() {
        Date date = (Date)getValue();
        return date == null ? "" : DATE_FORMAT.format(date);
    }

    @Override
    public void setAsText(String string) {
        try {
//          setValue(new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse(s));
            DATE_FORMAT.parse(string);
        } catch (ParseException pe) {
            throw new IllegalArgumentException("Could not parse date");
       }
    }
    
}
