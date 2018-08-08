package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.exception.ProcessException;
import au.com.trgtd.tr.sync.device.socket.send.SocketSenderException;
import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * High level message responder using a wrapped socket sender to send.
 *
 * @author Jeremy Moore
 */
public class Processor implements IProcessor
{
    private static final String MSG_NO_HANDLER = "No handler found for received message for code: ";
    
    private final Map<RecvMsg.Type, Handler<RecvMsg>> handlerMap;

    /**
     * Construct the message processor.
     * @param sender The message sender.
     * @throws SocketSenderException if there is an exception thrown by the
     * socket sender.
     */
    public Processor(ISender sender, DataMgr data) throws Exception
    {
        if (sender == null) {
            throw new SocketSenderException("Message sender is null.");
        }

        EnumMap<RecvMsg.Type, Handler<RecvMsg>> map
                = new EnumMap<RecvMsg.Type, Handler<RecvMsg>>(RecvMsg.Type.class);
        
        map.put(RecvMsg.Type.SUMMARY, new HandlerSummary(sender, data));
        map.put(RecvMsg.Type.NEWTHOUGHT, new HandlerNewThgt(sender, data));
        map.put(RecvMsg.Type.UPDACTION, new HandlerUpdActn(sender, data));
        map.put(RecvMsg.Type.UPDPROJECT, new HandlerUpdProject(sender, data));
        map.put(RecvMsg.Type.REFERENCE, new HandlerCngRef(sender, data));
        map.put(RecvMsg.Type.NEXTACTION, new HandlerNxtActn(sender, data));
        map.put(RecvMsg.Type.NEXTPROJECT, new HandlerNxtProject(sender, data));
        map.put(RecvMsg.Type.NEXTTOPIC, new HandlerNxtTopc(sender, data));
        map.put(RecvMsg.Type.NEXTCONTEXT, new HandlerNxtCntx(sender, data));
        map.put(RecvMsg.Type.NEXTTIME, new HandlerNxtTime(sender, data));
        map.put(RecvMsg.Type.NEXTENERGY, new HandlerNxtEngy(sender, data));
        map.put(RecvMsg.Type.NEXTPRIORITY, new HandlerNxtPrty(sender, data));
        map.put(RecvMsg.Type.NEXTREFERENCE, new HandlerNxtRef(sender, data));
        map.put(RecvMsg.Type.GOODBYE, new HandlerGoodBye(sender, data));
        map.put(RecvMsg.Type.CANCEL, new HandlerCancel(sender, data));

        this.handlerMap = Collections.unmodifiableMap(map);
    }

    @Override
    public void process(RecvMsg recvMsg) throws Exception {
        if (recvMsg == null) {
            throw new ProcessException("Message to process is null.");
        }
        Handler<RecvMsg> handler = handlerMap.get(recvMsg.type);
        if (handler == null) {
            throw new ProcessException(MSG_NO_HANDLER + recvMsg.type.toCode());
        }
        handler.handle(recvMsg);
    }
}