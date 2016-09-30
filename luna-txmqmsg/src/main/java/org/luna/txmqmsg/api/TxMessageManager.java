package org.luna.txmqmsg.api;

/**
 * Created by Administrator on 2016/9/13.
 */
public interface TxMessageManager {

    public String prepare(TxMessage txMessage);

    public void execute();

    public void execute(String txMessageId);

    public void cancel();

    public void cancel(String txMessageId);
}
