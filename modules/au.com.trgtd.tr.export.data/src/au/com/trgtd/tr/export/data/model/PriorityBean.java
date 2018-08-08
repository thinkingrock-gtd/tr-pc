package au.com.trgtd.tr.export.data.model;

public final class PriorityBean {
    
    private final int id;
    private final String title;
    private final Integer value;      // iCalendar mapped value
    private final int order;

    public PriorityBean(int id, String title, Integer value, int order) {
        this.id = id;
        this.title = title;
        this.value = value;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getValue() {
        return value;
    }

    public int getOrder() {
        return order;
    }
        
}
