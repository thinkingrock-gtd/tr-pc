package au.com.trgtd.tr.sync.device.v100.message.receive;

import java.util.Date;

/**
 * Update project message parser.
 */
public class RecvMsgProject extends RecvMsg {

    public final int id;
    public final String notes;
    public final String purpose;
    public final String vision;
    public final String brainstorm;
    public final String organise;
    public final Date dueDate;

    public RecvMsgProject(int id, 
            String notes, 
            String purpose, 
            String vision, 
            String brainstorm, 
            String organise, 
            Date dueDate) 
    {
        super(RecvMsg.Type.UPDPROJECT);
        this.id = id;
        this.notes = notes;
        this.purpose = purpose;
        this.vision = vision;
        this.brainstorm = brainstorm;
        this.organise = organise;
        this.dueDate = dueDate;
    }

}
