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
package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgSummary;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgCancel;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgSummary;
import au.com.trgtd.tr.util.Utils;

/**
 * Handle received summary message.
 *
 * @author Jeremy Moore
 */
class HandlerSummary extends Handler <RecvMsg> {

    private static final SendMsg cancel = new SendMsgCancel();
    

    HandlerSummary(ISender sender, DataMgr data) {
        super(sender, data);
    }

    @Override
    void handle(RecvMsg msg) throws Exception {

        RecvMsgSummary summary = (RecvMsgSummary)msg;

        data.setNbrRcvThoughts(summary.nNewThgts);
        data.setNbrUpdActns(summary.nUpdActns);
        data.setNbrUpdProjects(summary.nUpdPrjts);
        data.setNbrRcvRefs(summary.nReferences);

        boolean wrongDataID = summary.dataID != null &&
                !Utils.equal(summary.dataID, data.getDataID());

        if (wrongDataID) {
//            final Component parent = null;
//            final String title = NbBundle.getMessage(getClass(), "wrong_sync_file_id_title");
//            final Object messg = NbBundle.getMessage(getClass(), "wrong_sync_file_id_messg", data.getDataID(), summary.dataID);
//            final int dtype = JOptionPane.YES_NO_OPTION;
//            final int mtype = JOptionPane.QUESTION_MESSAGE;
//            final Icon icon = null;
//            final Object[] options = null;
//            final Object option = null;
//            int rslt = JOptionPane.showOptionDialog(parent, messg, title, dtype, mtype, icon, options, option);
//            if (rslt == JOptionPane.NO_OPTION) {
//                sender.send(cancel);
//                throw new DataIDException("Wrong Data ID and sync cancelled by user");
//            } 
            data.setNbrUpdActns(0);
            data.setNbrUpdProjects(0);
        }

        sender.send(new SendMsgSummary(data));
    }
    
}
