/*
 * ThinkingRock, a project management tool for Personal Computers.
 * Copyright (C) 2006 Avente Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
