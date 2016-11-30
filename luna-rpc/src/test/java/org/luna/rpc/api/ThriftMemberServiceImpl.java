package org.luna.rpc.api;

import org.apache.thrift.TException;
import org.luna.rpc.api.thrift.*;
import org.luna.rpc.api.thrift.Member;

/**
 * Created by luliru on 2016/11/30.
 */
public class ThriftMemberServiceImpl implements org.luna.rpc.api.thrift.MemberService.Iface {

    @Override
    public Member findById(long id) throws TException {
        System.out.println("ThriftMemberServiceImpl.findById invoke");
        Member m = new Member();
        m.setId(id);
        m.setUsername("luliru");
        return m;
    }

    @Override
    public void create(Member new_member) throws BizException, TException {
        System.out.println("ThriftMemberServiceImpl.create invoke .Member ="+new_member);
    }
}
