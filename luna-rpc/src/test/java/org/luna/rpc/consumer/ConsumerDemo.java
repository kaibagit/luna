package org.luna.rpc.consumer;

import org.apache.thrift.TException;
import org.luna.rpc.api.DemoService;
import org.luna.rpc.api.MemberService;
import org.luna.rpc.api.thrift.Member;
import org.luna.rpc.config.MethodConfig;
import org.luna.rpc.config.ProtocolConfig;
import org.luna.rpc.config.ReferenceConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luliru on 2016/11/17.
 */
public class ConsumerDemo {

    public static void main(String[] args) throws TException {
        List<MethodConfig> methodConfigs = new ArrayList<>();
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("hello");
        methodConfigs.add(methodConfig);

        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication("luna-rpc-demo");
        referenceConfig.setServiceClass(DemoService.class);
        referenceConfig.setVersion("1.0");
        referenceConfig.setProtocol("luna");
        referenceConfig.setUrls("localhost:6666");
        referenceConfig.setMethods(methodConfigs);

        DemoService demoService = referenceConfig.getRef();
        System.out.println(demoService.getById(1));
        System.out.println(demoService.hello("luna"));
        demoService.hello();

        ReferenceConfig<MemberService> referenceConfig2 = new ReferenceConfig<>();
        referenceConfig2.setApplication("luna-rpc-demo");
        referenceConfig2.setServiceClass(MemberService.class);
        referenceConfig2.setVersion("1.0");
        referenceConfig2.setUrls("localhost:6666");
        referenceConfig2.setProtocol("luna");

        MemberService memberService = referenceConfig2.getRef();
        memberService.findByParams(false,new Date(),new ArrayList<Integer>(){
            {
                add(1);
            }
        });
        System.out.println(memberService.getDescription(1));


        ReferenceConfig<org.luna.rpc.api.thrift.MemberService.Iface> referenceConfig3 = new ReferenceConfig<>();
        referenceConfig3.setApplication("luna-rpc-demo");
        referenceConfig3.setServiceClass(org.luna.rpc.api.thrift.MemberService.Iface.class);
        referenceConfig3.setProtocol("luna");
        referenceConfig3.setSerialization("thrift");
        referenceConfig3.setUrls("localhost:8090");
        org.luna.rpc.api.thrift.MemberService.Iface thriftMemservice = referenceConfig3.getRef();
        thriftMemservice.create(new Member(1L,"luliru",2));
        System.out.println(thriftMemservice.findById(1L));

    }

}
