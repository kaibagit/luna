package org.luna.rpc.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.luna.rpc.core.exception.LunaRpcException;
import org.luna.rpc.core.extension.Spi;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

/**
 * Created by luliru on 2016/11/23.
 */
@Spi(name = "hessian2")
public class Hessian2Serialization implements Serialization {

    @Override
    public byte[] serialize(Object data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Hessian2Output out = new Hessian2Output(bos);
            out.writeObject(data);
            out.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new LunaRpcException("hessian2 serialize error",e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] data, Class<T> clz){
        try {
            Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(data));
            return (T) input.readObject(clz);
        } catch (IOException e) {
            throw new LunaRpcException("hessian2 deserialize error",e);
        }
    }
}
