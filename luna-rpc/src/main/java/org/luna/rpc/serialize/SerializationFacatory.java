package org.luna.rpc.serialize;

import org.luna.rpc.common.constant.SerializationType;
import org.luna.rpc.core.LunaRpcException;

/**
 * Created by kaiba on 2016/5/30.
 */
public class SerializationFacatory {

    private SerializationFacatory(){};

    public static Serialization getInstance(int serializationType){
        switch (serializationType){
            case SerializationType.JSON:
                return new JsonSerialization();
            default:
                throw new LunaRpcException("unsupport serializationType");
        }
    }
}
