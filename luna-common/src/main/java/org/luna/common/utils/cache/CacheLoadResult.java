package org.luna.common.utils.cache;

/**
 * Created by luliru on 2016/6/3.
 */
public class CacheLoadResult<T> {

    private boolean hasData;

    private T data;

    public CacheLoadResult(boolean hasData,T data){
        this.hasData = hasData;
        this.data = data;
    }

    public boolean isHasData() {
        return hasData;
    }

    public T getData() {
        return data;
    }
}
