package org.luna.rpc.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by luliru on 2016/11/25.
 */
public interface MemberService {

    Member getById(int id);

    List<Member> findByParams(boolean sex, Date joinDate,List<Integer> excludeIds);

    Map<String,Object> getDescription(int id);

    String getByIdThrowUnknowException(int id);

    String getByIdThrowSpecifiedException(int id);

    String getByIdThrowRpcException(int id);
}
