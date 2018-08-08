package au.com.trgtd.tr.sync.device.v100.message.receive;

/**
 * Summary message.
 */
public class RecvMsgSummary extends RecvMsg {

    public final int nNewThgts;
    public final int nUpdActns;
    public final int nUpdPrjts;
    public final int nReferences;
    public final String dataID;

    public RecvMsgSummary(String dataID, int nNewThgts, int nUpdActns, int nUpdPrjts, int nReferences) {
        super(RecvMsg.Type.SUMMARY);
        this.dataID = dataID;
        this.nNewThgts = nNewThgts;
        this.nUpdActns = nUpdActns;
        this.nUpdPrjts = nUpdPrjts;
        this.nReferences = nReferences;
    }

}
