package org.luna.rpc.consumer;

import org.apache.thrift.TException;
import org.luna.rpc.api.DemoService;
import org.luna.rpc.api.MemberService;
import org.luna.rpc.api.thrift.Member;
import org.luna.rpc.config.ProtocolConfig;
import org.luna.rpc.config.ReferenceConfig;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by luliru on 2016/11/17.
 */
public class ConsumerDemo {

    public static void main(String[] args) throws TException {
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("luna");
        protocol.setHost("localhost");
        protocol.setPort(6666);

        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication("luna-rpc-demo");
        referenceConfig.setServiceClass(DemoService.class);
        referenceConfig.setVersion("1.0");
        referenceConfig.setDirectUrl("localhost:6666");
        referenceConfig.setProtocol(protocol);

        DemoService demoService = referenceConfig.getRef();
        System.out.println(demoService.getById(1));
        System.out.println(demoService.hello("luna"));
        demoService.hello();

        ReferenceConfig<MemberService> referenceConfig2 = new ReferenceConfig<>();
        referenceConfig2.setApplication("luna-rpc-demo");
        referenceConfig2.setServiceClass(MemberService.class);
        referenceConfig2.setVersion("1.0");
        referenceConfig2.setDirectUrl("localhost:6666");
        referenceConfig2.setProtocol(protocol);

        MemberService memberService = referenceConfig2.getRef();
        memberService.findByParams(false,new Date(),new ArrayList<Integer>(){
            {
                add(1);
            }
        });
        System.out.println(memberService.getDescription(1));


        ProtocolConfig thriftProtocol = new ProtocolConfig();
        thriftProtocol.setName("luna");
        thriftProtocol.setHost("localhost");
        thriftProtocol.setPort(8090);
        ReferenceConfig<org.luna.rpc.api.thrift.MemberService.Iface> referenceConfig3 = new ReferenceConfig<>();
        referenceConfig3.setApplication("luna-rpc-demo");
        referenceConfig3.setServiceClass(org.luna.rpc.api.thrift.MemberService.Iface.class);
        referenceConfig3.setProtocol(thriftProtocol);
        org.luna.rpc.api.thrift.MemberService.Iface thriftMemservice = referenceConfig3.getRef();
        thriftMemservice.create(new Member(1L,"luliru",2));
        System.out.println(thriftMemservice.findById(1L));

    }

}
