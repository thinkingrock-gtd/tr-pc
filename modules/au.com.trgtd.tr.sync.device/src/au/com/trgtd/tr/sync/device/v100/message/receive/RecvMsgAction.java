package au.com.trgtd.tr.sync.device.v100.message.receive;

import java.util.Date;

/**
 * Update action message parser.
 */
public class RecvMsgAction extends RecvMsg {

    public final int id;
    public final boolean done;
    public final Date doneDate; // added 2015/03/23
    public final String notes;
    public final Date date;
    public final Integer startHr;
    public final Integer startMn;
    public final Integer lengthHrs;
    public final Integer lengthMns;

    public RecvMsgAction(int id,
            boolean done,
            Date doneDate,
            String notes,
            Date date,
            Integer startHr,
            Integer startMn,
            Integer lengthHrs,
            Integer lengthMns) 
    {
        super(RecvMsg.Type.UPDACTION);
        this.id = id;
        this.done = done;
        this.doneDate = doneDate;
        this.notes = notes;
        this.date = date;
        this.startHr = startHr;
        this.startMn = startMn;
        this.lengthHrs = lengthHrs;
        this.lengthMns = lengthMns;
    }

}
