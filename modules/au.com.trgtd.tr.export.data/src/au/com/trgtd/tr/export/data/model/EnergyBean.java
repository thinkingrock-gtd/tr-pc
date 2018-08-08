package au.com.trgtd.tr.export.data.model;

public final class EnergyBean {
    
    private final int id;
    private final String title;
    private final int order;

    public EnergyBean(int id, String title, int order) {
        this.id = id;
        this.title = title;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getOrder() {
        return order;
    }
     
}
