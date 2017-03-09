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

    /** 限速，单位：个/秒 */
    rateLimit("rateLimit",""),

    /** 权重 */
    weight("weight",100),

    /** 工作线程，默认为CPU核数*2 */
    workerThread("workerThread",Runtime.getRuntime().availableProcessors() * 2),

    /** pool min conn number **/
    minClientConnection("minClientConnection", 2),
    /** pool max conn number **/
    maxClientConnection("maxClientConnection", 10),

    /** provider or consumer */
    side("side","");

    private String name;

    private String value;

    URLParamType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    URLParamType(String namem, Integer value){
        this.name = namem;
        if(value != null){
            this.value = value.toString();
        }
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public int getIntValue(){
        return Integer.valueOf(value);
    }

    public long getLongValue() {
        return Long.valueOf(value);
    }
}
