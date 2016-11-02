package org.luna.rpc.config;

/**
 * ApplicationConfig
 * Created by luliru on 2016/11/2.
 */
public class ApplicationConfig {

    private String name;

    public ApplicationConfig(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
