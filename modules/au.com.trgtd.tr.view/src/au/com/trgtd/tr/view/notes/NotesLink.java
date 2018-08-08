package au.com.trgtd.tr.view.notes;

public class NotesLink {
    
    public final String url;
    public final String text;

    /**
     * Constructs a new instance
     * @param url The link URL string which must not be null or an empty string.
     * @param text the human readable text for the link display, which can be
     * null to display the URL.
     */
    public NotesLink(String url, String text) {
        if (url == null || url.trim().length() == 0) {
            throw new IllegalArgumentException("Missing notes link URL.");
        }
        this.url = url.trim();
        this.text = (text != null && text.trim().length() > 0) ? text.trim() : null;
    }

    public String encode() {
        return text == null ? "[" + url + "]" : "[" + url + "|" + text + "]";
    }
    
}
