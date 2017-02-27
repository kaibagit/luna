package org.luna.rpc.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.luna.rpc.api.DemoService;
import org.luna.rpc.api.MemberService;
import org.luna.rpc.api.MemberSuspendException;
import org.luna.rpc.config.MethodConfig;
import org.luna.rpc.config.ProtocolConfig;
import org.luna.rpc.config.ReferenceConfig;
import org.luna.rpc.core.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by luliru on 2016/12/8.
 */
public class GetExceptionsConsumerDemo {

    private static Logger logger = LoggerFactory.getLogger(GetExceptionsConsumerDemo.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ReferenceConfig<MemberService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setGroup("luna-rpc-demo");
        referenceConfig.setServiceClass(MemberService.class);
        referenceConfig.setVersion("1.0");
        referenceConfig.setDirect("localhost:6666");
        referenceConfig.setProtocol("luna");

        try{
            MemberService memberService = referenceConfig.getRef();
//        memberService.getByIdThrowRpcException(1);
//            memberService.getByIdThrowSpecifiedException(1);
//        memberService.getByIdThrowUnknowException(1);
            memberService.getByIdThrowJvmException(1);
        }catch (Exception e){
            logger.error("There is a error",e);
        }


    }
}
