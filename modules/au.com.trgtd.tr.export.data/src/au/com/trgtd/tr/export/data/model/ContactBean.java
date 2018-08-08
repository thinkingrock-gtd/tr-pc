package au.com.trgtd.tr.export.data.model;

public final class ContactBean {
    
    private final int id;
    private final String name;
    private final String email;
    private final boolean inactive;
    private final boolean team;

    public ContactBean(int id, String name, String email, boolean team, boolean inactive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.team = team;
        this.inactive = inactive;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isInactive() {
        return inactive;
    }

    public boolean isTeam() {
        return team;
    }
    
}
