package org.luna.rpc.provider;

import org.luna.rpc.api.Member;
import org.luna.rpc.api.MemberService;

import java.util.*;

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
}
