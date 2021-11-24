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
