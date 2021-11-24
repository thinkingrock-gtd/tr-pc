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
package au.com.trgtd.tr.sync.device.v100;

import au.com.trgtd.tr.sync.device.ISyncHandler;
import au.com.trgtd.tr.sync.device.SyncState;
import au.com.trgtd.tr.sync.device.exception.CancelException;
import au.com.trgtd.tr.sync.device.socket.receive.ISocketReceiver;
import au.com.trgtd.tr.sync.device.socket.send.ISocketSender;
import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.process.IProcessor;
import au.com.trgtd.tr.sync.device.v100.message.process.Processor;
import au.com.trgtd.tr.sync.device.v100.message.receive.IReceiver;
import au.com.trgtd.tr.sync.device.v100.message.receive.Receiver;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import static au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg.Type.GOODBYE;
import static au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg.Type.SUMMARY;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgCancel;
import au.com.trgtd.tr.sync.device.v100.message.send.Sender;

public class SyncHandlerV100 implements ISyncHandler {

    private final DataMgr data;
    private final IProcessor processor;
    private final IReceiver receiver;
    private final ISender sender;
    private final SyncState state;

    private boolean cancel;

    public SyncHandlerV100(ISocketReceiver sr, ISocketSender ss, SyncState state)
            throws Exception
    {
        this.data = new DataMgr();
        this.receiver = new Receiver(sr);
        this.sender = new Sender(ss);
        this.processor = new Processor(sender, data);
        this.state = state;
    }

    @Override
    public void sync() throws Exception
    {
        cancel = false;
        
        // receive and respond to summary message
        RecvMsg msg = receiver.recv(SUMMARY);
        processor.process(msg);

        // set progress data
        int todo = data.getTotal();
        int done = 0;        
        state.setProgressValues(todo, done);

        // receive and respond to subsequent messages until goodbye message
        do {            
            if (cancel) {
                sender.send(new SendMsgCancel());
                throw new CancelException();
            }

            msg = receiver.recv();

            processor.process(msg);

            state.incDone();

        } while (msg.type != GOODBYE);

        
        data.commitDataChanges();
    }

    @Override
    public void cancel() {
        cancel = true;
    }

}
