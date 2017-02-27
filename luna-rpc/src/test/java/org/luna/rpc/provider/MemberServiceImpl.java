package org.luna.rpc.provider;

import java.util.*;

import org.luna.rpc.api.Member;
import org.luna.rpc.api.MemberService;
import org.luna.rpc.api.MemberSuspendException;
import org.luna.rpc.core.exception.RateLimitingException;

/**
 * Created by luliru on 2016/11/25.
 */
public class MemberServiceImpl implements MemberService {

    @Override
    public Member getById(int id) {
        return null;
    }

    @Override
    public List<Member> findByParams(boolean sex, Date joinDate, List<Integer> excludeIds) {
        System.out.println("sex = "+sex + " ,joinDate = "+joinDate+" ,excludeIds = "+excludeIds);
        List<Member> list = new ArrayList<>();
        return list;
    }

    @Override
    public Map<String, Object> getDescription(int id) {
        Map<String,Object> map = new HashMap<>();
        map.put("name","luliru");
        return map;
    }

    @Override
    public String getByIdThrowUnknowException(int id) {
        throw new EmbeddedMemberException();
//        return null;
    }

    @Override
    public String getByIdThrowSpecifiedException(int id) {
        throw new MemberSuspendException();
    }

    @Override
    public String getByIdThrowRpcException(int id) {
        throw new RateLimitingException();
    }

    @Override
    public String getByIdThrowJvmException(Integer id) {
        throw new IllegalArgumentException("id 不能为空");
    }
}
