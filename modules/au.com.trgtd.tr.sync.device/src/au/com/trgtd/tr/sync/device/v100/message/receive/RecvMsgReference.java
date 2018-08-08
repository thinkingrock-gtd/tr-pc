package au.com.trgtd.tr.sync.device.v100.message.receive;

/**
 * Receive reference message parser.
 */
public class RecvMsgReference extends RecvMsg {

    private int id;
    public final String title;
    public final String notes;
    public final Integer topicID;
    public final String change;

    public RecvMsgReference(int id, String title, String notes, Integer topicID, String change) {
        super(RecvMsg.Type.REFERENCE);
        this.id = id;
        this.title = title;
        this.notes = notes;
        this.topicID = topicID;
        this.change = change;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
