package au.com.trgtd.tr.sync.device.v100.message.receive;

/**
 * New thought message parser.
 */
public class RecvMsgThought extends RecvMsg {

    public final String title;
    public final String notes;
    public final int topicID;

    public RecvMsgThought(String title, String notes, int topicID) {
        super(RecvMsg.Type.NEWTHOUGHT);
        this.title = title;
        this.notes = notes;
        this.topicID = topicID;
    }

}
