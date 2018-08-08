package au.com.trgtd.tr.export.data.model;

public final class ContextBean {
    
    private final int id;
    private final String title;
    private final String descr;

    public ContextBean(int id, String title, String descr) {
        this.id = id;
        this.title = title;
        this.descr = descr;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescr() {
        return descr;
    }
        
}
