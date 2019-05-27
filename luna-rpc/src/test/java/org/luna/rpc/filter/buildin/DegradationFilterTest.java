package org.luna.rpc.filter.buildin;

import org.junit.Test;
import org.luna.rpc.core.*;
import org.luna.rpc.core.exception.RateLimitingException;

import java.util.Map;

/**
 * Created by luliru on 2017/3/20.
 */
public class DegradationFilterTest {

    @Test
    public void test() throws InterruptedException {
        Client client = new MockClient();
        Invocation invocation = new MockInvocation();
        DegradationFilter filter = new DegradationFilter();
        for(int i=0;i<10;i++){
            try{
                System.out.println(filter.filter(client,invocation).getValue());
            }catch (Exception e){
                e.printStackTrace();
            }
            Thread.sleep(200L);
        }
    }
}

class MockClient implements Client{

    @Override
    public Result call(Invocation invocation) {
        Result result = new Result() {

            private RateLimitingException e = new RateLimitingException();

            @Override
            public Object getValue() {
                return e;
            }

            @Override
            public Exception getException() {
                return e;
            }

            @Override
            public boolean hasException() {
                return true;
            }
        };
        return result;
    }

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }
}
class MockInvocation implements Invocation{

    @Override
    public String getGroup() {
        return "test_group";
    }

    @Override
    public String getServiceName() {
        return "test_service";
    }

    @Override
    public String getVersion() {
        return "test_version";
    }

    @Override
    public String getMethodName() {
        return "test_method";
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return new Class<?>[0];
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public Map<String, String> getAttachments() {
        return null;
    }

    @Override
    public String getAttachment(String key) {
        return null;
    }

    @Override
    public String getAttachment(String key, String defaultValue) {
        return null;
    }
}