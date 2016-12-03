package org.luna.rpc.config;

/**
 * 方法配置
 * Created by luliru on 2016/10/14.
 */
public class MethodConfig {

    // 方法名
    private String name;
    // 超时时间
    private Integer requestTimeout;
    // 失败重试次数（默认为0，不重试）
    private int retries = 0;
    // 异步
    private boolean async = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public boolean isAsync() {
        return async;
    }
}
