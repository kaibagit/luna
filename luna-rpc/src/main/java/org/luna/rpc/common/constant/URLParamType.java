package org.luna.rpc.common.constant;

/**
 * Created by luliru on 2016/11/14.
 */
public enum URLParamType {

    /** codec */
    codec("codec", "luna"),

    /** serialize */
    serialize("serialization", "hessian2");

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
}
