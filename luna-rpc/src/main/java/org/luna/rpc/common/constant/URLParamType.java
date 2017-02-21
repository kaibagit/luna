package org.luna.rpc.common.constant;

/**
 * Created by luliru on 2016/11/14.
 */
public enum URLParamType {

    group("group", "default_group"),

    /** codec */
    codec("codec", "luna"),

    /** serialize */
    serialize("serialization", "hessian2"),

    /** request timeout **/
    requestTimeout("requestTimeout", "500"),

    /** async */
    async("async","false"),

    /** 工作线程，默认为CPU核数*2 */
    workerThread("workerThread",Integer.valueOf(Runtime.getRuntime().availableProcessors() * 2).toString()),

    /** provider or consumer */
    side("side",null);

    private String name;

    private String value;

    private URLParamType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public long getLongValue() {
        return Long.valueOf(value);
    }
}
